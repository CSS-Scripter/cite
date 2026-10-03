import j from "jsonwebtoken";
import { InvalidToken } from "../models/invalidToken";
import { User, UserDoc } from "../models/user";

interface UserPayload {
  id: string;
  email: string;
  username: string;
}

class JWTService {
  sign(payload: UserPayload) {
    return j.sign(payload, process.env.JWT_KEY!, { expiresIn: process.env.JWT_EXPIRES_IN });
  }

  async verify(token: string): Promise<UserDoc | null | undefined> {
    const invalidToken = await InvalidToken.findOne({ token });
    if (invalidToken) {
      return null;
    }

    const user = j.verify(token, process.env.JWT_KEY!) as UserPayload;
    return await User.findById(user.id);
  }

  async signout(token: string) {
    const invalidToken = InvalidToken.build({ token });
    await invalidToken.save();
  }
}

export const jwt = new JWTService();
