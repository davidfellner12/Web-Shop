export interface Customer {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: Date;
}

export interface CustomerListDto {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: Date;
  age: number;
}

export interface CustomerSearch {
  firstName?: string;
  lastName?: string;
  email?: string;
  dateOfBirthEarliest?: string;
  dateOfBirthLatest?: string;
  minAge?: number;
  maxAge?: number;
}
