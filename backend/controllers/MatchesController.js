import { ApiError } from "../helpers/ApiError";

//https://firebase.google.com/docs/firestore/quickstart

const generateMatches = async (req, res, next) => {
  try {
    const uid = req.user.uid;
    const userRef = db.collection("users").doc(uid);
    const userDoc = await userRef.get();
    if (!userDoc.exists) throw new ApiError("Doc not found", 404);
    const interests = userDoc.data().interests;
    console.log(interests);

    const potentialMatches = await findByInterests(interests);
    /*TODO: Implement matching*/

    return res.status(200).json({ message: "New matches generated" });
  } catch (error) {
    return next();
  }
};

const findByInterests = async (interests) => {
  if (interests) return ["user1", "user2"];
};

export { generateMatches };
