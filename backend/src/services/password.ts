import { randomBytes, scrypt } from 'crypto';
import { promisify } from 'util';

const scryptAsync = promisify(scrypt)

export class Password {
    /**
     * Encrypt a password using a hash and salt.
     * @param password the password that needs to be encrypted
     */
    static async toHash(password: string): Promise<String> {
        const salt = randomBytes(8).toString('hex');
        const buffer = await scryptAsync(password, salt, 64) as Buffer;
        return `${buffer.toString('hex')}.${salt}`
    }

    /**
     * Confirms the password enterd by the user matches the stored one in the database.
     * @param storedPassword the password from the database
     * @param suppliedPassword the password entered by the user 
     */
    static async compare(storedPassword: string, suppliedPassword: string): Promise<boolean> {
        const [hashedPassword, salt] = storedPassword.split('.');
        const buffer = await scryptAsync(suppliedPassword, salt, 64) as Buffer;
        return buffer.toString('hex') === hashedPassword
    }
}