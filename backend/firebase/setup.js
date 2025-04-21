import { initializeApp, applicationDefault } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";

//https://firebase.google.com/docs/projects/api/workflow_set-up-and-manage-project#linux-or-macos
//Linux: export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your/service-account-file.json"
//PS: $env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\your\service-account-file.json"

const app = initializeApp({
  credential: applicationDefault(),
});

const db = getFirestore();

console.log("firebase initialized")

export { app, db };
