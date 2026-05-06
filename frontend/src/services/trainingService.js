import API from "./api";

export const getAllTrainings = () => API.get("/trainings");
export const getTrainingById = (id) => API.get(`/trainings/${id}`);
export const createTraining = (data) => API.post("/trainings", data);
export const updateTraining = (id, data) => API.put(`/trainings/${id}`, data);
export const deleteTraining = (id) => API.delete(`/trainings/${id}`);
export const getDashboardMetrics = () => API.get("/trainings/dashboard");