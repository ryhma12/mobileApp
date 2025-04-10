import { Router } from "express";
import { generateMatches } from "../controllers/MatchesController";
import { auth } from "../firebase/auth";

const router = Router();

router.post("/generate", auth, generateMatches);

export default router;
