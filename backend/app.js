import express from "express";

import matchesRouter from "./routes/MatchesRouter.js";
import bioRouter from "./routes/BioRouter.js"

const app = express();

const port = 3001;

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use("/matches", matchesRouter);
app.use("/bio", bioRouter)

app.use((err, req, res, next) => {
  const statusCode = err.statusCode || 500;
  res.status(statusCode).json({ error: err.message });
});

app.listen(port);