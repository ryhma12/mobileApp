import { getAuth } from "firebase-admin/auth";
import { ApiError } from "../helpers/ApiError.js";
const authorizationRequired = "Authorization required";
const invalidCredentials = "Invalid credentials";

const auth = async(req, res, next) => {
  const idToken = req.headers.authorization;
  if (!idToken) return next(new ApiError(authorizationRequired, 401));
  getAuth()
    .verifyIdToken(idToken)
    .then((decodedToken) => {
      req.user = { uid: decodedToken.uid };
      next();
    })
    .catch((error) => {
      next(new ApiError(invalidCredentials, 403));
    });
};

export { auth };
