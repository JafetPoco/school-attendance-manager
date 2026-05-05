import type { StudentRequest } from './Student';

export interface ParentWithChildrenRequest {
    names: string
    phoneNumber: string
    children: StudentRequest[]
}