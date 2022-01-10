package view.helpers;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

/**
 * Класс для сохранения ширины региона внутри SplitPane
 * При удалении потомка из SplitPane его DividerPosition не сохраняется и при добавлении он занимает половину ширины.
 * Данный класс сохраняет ширину региона перед его удалением из SplitPane и по возможности восстанавливает её после добавления.
 */
public class SPSaveWidthHelper {
    private final SplitPane panelsRoot;
    private final Region targetRegion;
    private double lastTargetRegionWidth;
    private double lastDividerWidth = 0;

    public SPSaveWidthHelper(SplitPane panelsRoot, Region targetRegion, double initWidth){
        this.targetRegion = targetRegion;
        this.panelsRoot = panelsRoot;
        SplitPane.setResizableWithParent(targetRegion, false);
        lastTargetRegionWidth = initWidth;
    }

    public void beforeRemoveRegion(){
        lastTargetRegionWidth = targetRegion.getWidth();

        ObservableList<Node> items = panelsRoot.getItems();
        if(items.size() > 0) {
            lastDividerWidth = panelsRoot.getWidth();
            for (Node node : items) {
                if (node instanceof Region) {
                    lastDividerWidth -= ((Region) node).getWidth();
                }
            }
            lastDividerWidth = lastDividerWidth / (items.size() - 1);
        }
    }

    public void afterAddRegion(int regionIdx){
        double dividerPos = lastTargetRegionWidth / (panelsRoot.getWidth() - lastDividerWidth * (panelsRoot.getItems().size() - 1));
        if(regionIdx > 0) {
            //TODO support middle regions
            dividerPos = 1 - dividerPos;
            panelsRoot.setDividerPosition(regionIdx - 1, dividerPos);
        } else {
            //TODO test
            panelsRoot.setDividerPosition(0, dividerPos);
        }
    }
}
