export interface UserDetails {
  username: string;
  roles: string[];
}

export interface UsersNotification {
  message: string;
  userId?: string;
}
