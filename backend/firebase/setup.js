import { initializeApp } from "firebase-admin/app";
import { getFirestore } from "firebase-admin/firestore";

//https://firebase.google.com/docs/projects/api/workflow_set-up-and-manage-project#linux-or-macos
//Linux: export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your/service-account-file.json"
//PS: $env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\your\service-account-file.json"

initializeApp({
  credential: applicationDefault(),
});
const db = getFirestore();

export { app, db };
