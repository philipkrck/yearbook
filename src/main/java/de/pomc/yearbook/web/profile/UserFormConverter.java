package de.pomc.yearbook.web.profile;

import de.pomc.yearbook.user.User;
import de.pomc.yearbook.web.profile.UserForm;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public abstract class UserFormConverter {

    public static UserForm userForm(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String twitterHandle = user.getTwitterHandle() != null ? user.getTwitterHandle() : "";
        String location = user.getLocation() != null ? user.getLocation() : "";
        String website = user.getWebsite() != null ? user.getWebsite() : "";
        String bio = user.getBio();
        byte[] image = user.getImage();

        return new UserForm(firstName, lastName, email, twitterHandle, location, website, bio, image);
    }

    public static User update(User user, UserForm userForm) {
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setTwitterHandle(userForm.getTwitterHandle());
        user.setLocation(userForm.getLocation());
        user.setWebsite(userForm.getWebsite());
        user.setBio(userForm.getBio());
        user.setImage(userForm.getImage());
        return user;
    }
}
