package helper;

import com.gembox.spreadsheet.ExcelFile;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelperFunctions {

    public static void fileChooserSave(ActionEvent event, ExcelFile workbook) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Window stage = ((Node)event.getSource()).getScene().getWindow();
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("ranking");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("excel file", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);
        fileChooser.setInitialDirectory(file.getParentFile());
        Path path = Paths.get(file.getAbsolutePath());
        workbook.save(String.valueOf(path));
    }
}
