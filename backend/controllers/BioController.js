import OpenAI from "openai";
import { zodResponseFormat } from "openai/helpers/zod";
import { z } from "zod";
import { ApiError } from "../helpers/ApiError.js";
import { isRateLimited } from "../helpers/RateLimit.js";
import dotenv from 'dotenv'
dotenv.config()

const GEMINI_API_KEY = process.env.GEMINI_API_KEY

if(!GEMINI_API_KEY){ throw new Error("NO API KEY FOUND")}

const openai = new OpenAI({
  apiKey: GEMINI_API_KEY,
  baseURL: "https://generativelanguage.googleapis.com/v1beta/openai",
});

const bioValidation = z.object({
  isValid: z.boolean({
    required_error: "isValid is required",
    invalid_type_error: "isValid must be a boolean",
  }),
  rating: z.number().int().positive().lte(5, {
    required_error: "rating is required",
    invalid_type_error: "rating must be a number",
    message: "rating must be positive and max 5",
  }),
  tags: z.string().array().length(3),
});

const validateBio = async (req, res, next) => {
  try {
    const uid = "jesse123dd"
    //const uid = req.user.uid;
    //const userRef = db.collection("users").doc(uid);
    //const userDoc = await userRef.get();
    //if (!userDoc.exists) throw new ApiError("Doc not found", 404);
    //const bio = userDoc.data().bio;

    const bio = req.body.bio;

    if (!bio || bio.trim().length === 0){
      throw new ApiError("Bio not found", 404);
    }
    if(isRateLimited(uid)){
      throw new ApiError("Upload rate limited", 429)
    }
    const result = await getBioValidation(bio);
    return res.status(200).json(result);
  } catch (error) {
    console.log(error);
    return next();
  }
};

const getBioValidation = async (bio) => {

  if (typeof bio !== "string") {
    throw new ApiError("Bio must be a string");
  }
  const bioString = bio
  console.log(bioString)
  const completion = await openai.beta.chat.completions.parse({
    model: "gemini-2.0-flash",
    messages: [
      {
        role: "system",
        content: "1: Check if bio is valid. 2: Rate the bio from 1 to 5. 3: Generate 3 relevant tags."
      },
      { role: "user", content: bioString },
    ],
    response_format: zodResponseFormat(bioValidation, "validation"),
  });
  const validation = completion.choices[0].message.parsed;

  console.log(validation)
  return validation;
};

export { validateBio };
