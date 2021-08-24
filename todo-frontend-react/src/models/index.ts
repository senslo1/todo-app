export interface ITodo {
    id?: number;
    text: string;
    category: string;
}

export interface IApiError {
    status: string;
    correlationId: string;
    message: string;
    debugMessage?: any;
    timestamp: string;
}