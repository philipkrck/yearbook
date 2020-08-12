package de.pomc.yearbook.web.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewModel {

    @Getter
    private String username;

    @Getter
    private String email;

    @Getter
    private String bio;
}
