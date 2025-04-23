import { ApiError } from "../helpers/ApiError.js";

const generateMatches = async (req, res, next) => {
  /*try {
    const uid = req.user.uid;
    const userRef = db.collection("users").doc(uid);
    const userDoc = await userRef.get();
    if (!userDoc.exists) throw new ApiError("Doc not found", 404);
    const tags = userDoc.data().tags;
    console.log(tags);
    const potentialMatches = await findByTags(tags);
    //TODO: Implement matching

    return res.status(200).json({ message: "New matches generated" });
  } catch (error) {
    return next();
  }*/
};


export { generateMatches };