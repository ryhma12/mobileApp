import express from "express";

import matchesRouter from "./routes/MatchesRouter.js";
import bioRouter from "./routes/BioRouter.js"

const app = express();


app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use("/matches", matchesRouter);
app.use("/bio", bioRouter)

app.use((err, req, res, next) => {
  const statusCode = err.statusCode || 500;
  res.status(statusCode).json({ error: err.message });
});

const port = process.env.PORT || 3001;
app.listen(port, () => {
  console.log(`Listening on port ${port}...`);
});