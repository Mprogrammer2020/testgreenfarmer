package com.greenelegentfarmer.util;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

	public String generatePassayPassword() {
		  CharacterRule alphabets = new CharacterRule(EnglishCharacterData.Alphabetical);
	      CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);

	      PasswordGenerator passwordGenerator = new PasswordGenerator();
	      String password = passwordGenerator.generatePassword(16, alphabets, digits);
	      return password;
	}
}
