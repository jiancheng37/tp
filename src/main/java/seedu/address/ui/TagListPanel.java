package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.search.SearchContext;
import seedu.address.model.tag.Tag;

/**
 * Panel containing the list of tags.
 */
public class TagListPanel extends UiPart<Region> {
    private static final String FXML = "TagListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TagListPanel.class);

    @FXML
    private FlowPane tags; // Use FlowPane instead of ListView

    /**
     * Creates a {@code TagListPanel} with the given {@code ObservableList}.
     */
    public TagListPanel(ObservableList<Tag> tagList, SearchContext searchContext) {
        super(FXML);
        tagList.addListener((ListChangeListener<Tag>) change -> updateTags(tagList, searchContext));
        updateTags(tagList, searchContext); // Update the FlowPane with the tags
    }

    /**
     * Updates the FlowPane with the given list of tags.
     */
    private void updateTags(ObservableList<Tag> tagList, SearchContext searchContext) {
        tags.getChildren().clear(); // Clear existing tags
        ObservableList<Tag> sortedTagList = tagList.sorted();
        for (Tag tag : sortedTagList) {
            Label tagLabel = new Label(tag.tagName + " (" + tag.getNumUsage() + ")");
            tags.getChildren().add(tagLabel); // Add each tag as a Label
        }
    }
}
