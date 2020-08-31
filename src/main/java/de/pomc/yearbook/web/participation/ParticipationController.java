package de.pomc.yearbook.web.participation;

import de.pomc.yearbook.SampleData;
import de.pomc.yearbook.book.BookService;
import de.pomc.yearbook.participation.Comment;
import de.pomc.yearbook.participation.Participation;
import de.pomc.yearbook.participation.ParticipationService;
import de.pomc.yearbook.user.UserService;
import de.pomc.yearbook.web.questionnaire.CommentForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("participation/{id}")
@RequiredArgsConstructor
public class ParticipationController {

    private final BookService bookService;
    private final ParticipationService participationService;
    private final UserService userService;

    @GetMapping
    public String showParticipationView(Model model, @PathVariable("id") Long id) {
        Participation participation = participationService.getParticipationWithID(id);

        // ToDo: make sure that user is either part of the book of this participation or the book is published

        model.addAttribute("participation", participation);
        // ToDo: use participation.getBook() in future
        model.addAttribute("book", SampleData.getBooks().get(0));

        // ToDo: add commentForm only if user is part of the book
        model.addAttribute("commentForm", new CommentForm());

        return "pages/participation/show";
    }

    @PreAuthorize("authenticated")
    @PostMapping("/addComment")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute("commentForm") CommentForm commentForm) {

        // Todo: check that current user is participant of book of this participation

        Comment comment = new Comment(commentForm.getComment(), userService.findCurrentUser());
        participationService.addComment(comment);

        return "redirect:/participation/{id}";
    }

}
