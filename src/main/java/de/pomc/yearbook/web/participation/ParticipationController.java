package de.pomc.yearbook.web.participation;

import de.pomc.yearbook.book.Book;
import de.pomc.yearbook.book.BookService;
import de.pomc.yearbook.participation.Comment;
import de.pomc.yearbook.participation.Participation;
import de.pomc.yearbook.participation.ParticipationService;
import de.pomc.yearbook.user.UserService;
import de.pomc.yearbook.web.exceptions.ForbiddenException;
import de.pomc.yearbook.web.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("participation/{id}")
@RequiredArgsConstructor
public class ParticipationController {

    private final BookService bookService;
    private final ParticipationService participationService;
    private final UserService userService;

    @ModelAttribute("participation")
    public Participation getParticipation(@PathVariable("id") Long id) {
        Participation participation = participationService.getParticipationWithID(id);
        if (participation == null) {
            throw new NotFoundException();
        }

        Book book = participation.getBook();
        if (!book.isPublished() && !book.currentUserHasParticipation()) {
            throw new ForbiddenException();
        }

        return participation;
    }

    @ModelAttribute("book")
    public Book getBook(@PathVariable("id") Long id) {
        return getParticipation(id).getBook();
    }

    @GetMapping
    public String showParticipationView(Model model, @PathVariable("id") Long id) {
        Participation participation = getParticipation(id);

        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("showEditButton", participation.currentUserIsParticipant());

        return "pages/participation/show";
    }

    @PreAuthorize("authenticated")
    @PostMapping("/addComment")
    public String addComment(@PathVariable("id") Long id, @ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult bindingResult) {
        Participation participation = getParticipation(id);

        if (!participation.currentUserCanComment()) {
            throw new ForbiddenException();
        }

        if(bindingResult.hasErrors()){
            return "pages/participation/show";
        }

        Comment comment = new Comment(commentForm.getComment(), userService.findCurrentUser());
        participationService.addComment(comment, participation);

        return "redirect:/participation/{id}";
    }

}
