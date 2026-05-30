export type User = {
  uuid?: string;
  cedula: string;
  nombres?: string;
  apellidos?: string;
  correo: string;
  telefono?: string;
  rol?: string;
  tenantId?: string;
  tipoNegocio?: string;
  fotoPerfil?: string;
};

export type AuthResponse = {
  accessToken: string;
  refreshToken: string;
  expiresInSeconds: number;
  rol: string;
  tenantId: string;
  usuario: User;
};

export type LoginInput = {
  correo: string;
  password: string;
};

export type RegisterInput = {
  cedula: string;
  nombres: string;
  apellidos: string;
  correo: string;
  telefono: string;
  password: string;
};

export type SessionState = {
  accessToken: string;
  refreshToken: string;
  expiresAt: number;
  user: User;
  tenantId: string;
  rol: string;
};
