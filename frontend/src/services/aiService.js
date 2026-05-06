import API from "./api";

export const describeTraining = (text) =>
  API.post("/ai/describe", { text });

export const recommendTraining = (text) =>
  API.post("/ai/recommend", { text });