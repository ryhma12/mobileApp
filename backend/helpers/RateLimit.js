const uidTimeMap = new Map();
const RATE_LIMIT = 5000;

const isRateLimited = (uid) => {
  const date = new Date();
  const currTime = date.getTime();

  const lastTime = uidTimeMap.get(uid);
  if (lastTime && currTime - lastTime < RATE_LIMIT) {
    return true;
  }
  uidTimeMap.set(uid, currTime);
  return false;
};

export { isRateLimited };
