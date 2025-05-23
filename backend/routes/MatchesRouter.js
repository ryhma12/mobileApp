import { Router } from "express";
import { auth } from "../firebase/auth.js";
import { generateMatches } from "../controllers/MatchesController.js";

const router = Router();

router.post("/generate", auth, generateMatches);

export default router;
