import express from "express";
import cron from "node-cron"
import matchesRouter from "./routes/MatchesRouter.js";
import bioRouter from "./routes/BioRouter.js"
import { generateMatchesInBatch, updateTagCount } from "./services/matchUpdater.js";

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

cron.schedule("0-59 * * * *", async() => {
  console.log("1 MINUTE");
  await updateTagCount()
  await generateMatchesInBatch()
});