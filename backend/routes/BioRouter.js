import { Router } from "express";
import { auth } from "../firebase/auth.js";
import { validateBio } from "../controllers/BioController.js";

const router = Router();

router.post("/validate", validateBio);

export default router;
