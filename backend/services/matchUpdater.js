import { db } from "../firebase/setup.js";

let tagCount = {};

const updateTagCount = async () => {
  // TODO: Update tagCount in bioValidation
  // User needs to have only unique tags
  tagCount = {};
  try {
    const snapshot = await db.collection("users").select("tags").get();
    snapshot.forEach((doc) => {
      const tags = doc.data().tags;
      if (Array.isArray(tags)) {
        tags.forEach((tag) => {
          tagCount[tag] = (tagCount[tag] || 0) + 1;
        });
      }
    });
  } catch (error) {
    console.log(error);
  }
};

const generateMatchesInBatch = async () => {
  try {
    const userData = await getUserDataBatch();
    const userCount = userData.length;

    const tagWeights = calcWeights(tagCount, userCount);

    const userVectors = userData.map((user) => {
      const tags = user.tags;
      const weights = tags.map((tag) => tagWeights[tag] || 0);
      return {
        uid: user.uid,
        tags: new Set(tags),
        magnitude: Math.hypot(...weights),
      };
    });

    //const matchScores = [];

    for (let i = 0; i < userVectors.length; i++) {
      const userA = userVectors[i];
      for (let j = i + 1; j < userVectors.length; j++) {
        const userB = userVectors[j];

        const overlappingTags = [];
        userB.tags.forEach((tag) => {
          if (userA.tags.has(tag)) overlappingTags.push(tag);
        });
        if (overlappingTags.length === 0) continue;

        // sum of squared IDF weights
        const dotProduct = overlappingTags.reduce(
          (sum, tag) => sum + tagWeights[tag] ** 2,
          0
        );

        // cosine similarity
        const score = dotProduct / (userA.magnitude * userB.magnitude);
        if (score > 0) {
          //matchScores.push({ userA: userA.uid, userB: userB.uid, score });
          try {
            const users = [userA.uid, userB.uid].sort();
            const matchId = `${users[0]}_${users[1]}`;
            const matchRef = db.collection("matches").doc(matchId);

            const matchData = {
              users,
              userA: userA.uid,
              userB: userB.uid,
              score,
              tags: overlappingTags,
            };
            await matchRef.set(matchData, { merge: true });
          } catch (error) {
            console.error("Failed to write match: ", userA.uid, error);
          }
        }
      }
    }

    matchScores.sort((a, b) => b.score - a.score);
    console.log(matchScores);
  } catch (error) {
    console.log(error);
  }
};

const getUserDataBatch = async () => {
  //TODO: Select recently logged in users
  const snapshot = await db.collection("users").select("tags").get();
  const users = [];

  snapshot.forEach((doc) => {
    const tags = doc.data().tags;
    if (tags && Array.isArray(tags)) {
      users.push({
        uid: doc.id,
        tags,
      });
    }
  });
  return users;
};

const calcWeights = (tagCount, userCount) => {
  const weights = {};

  Object.keys(tagCount).forEach((tag) => {
    weights[tag] = Math.log(userCount / (tagCount[tag] + 1));
  });
  return weights;
};

export { generateMatchesInBatch, updateTagCount };
