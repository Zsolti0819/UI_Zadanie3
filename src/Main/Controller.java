package Main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;

public class Controller {

    @FXML
    private JFXTextField riesenieKroky;

    @FXML
    private JFXTextField riesenieFitness;

    @FXML
    private JFXTextArea rieseniePohyb;

    @FXML
    private JFXTextField rieseniePoklady;

    @FXML
    private JFXTextField mriezkaY;

    @FXML
    private JFXTextField mriezkaX;

    @FXML
    private JFXTextField pocetPokladov;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXTextField startY;

    @FXML
    private JFXTextField startX;

    @FXML
    private JFXTextArea suradnicePokladov;

    @FXML
    private JFXTextField mutaciaField;

    @FXML
    void najdiRiesenie(ActionEvent event) {

        Mapa mapa = new Mapa(
                Integer.parseInt(pocetPokladov.getText()),
                Integer.parseInt(mriezkaX.getText()),
                Integer.parseInt(mriezkaY.getText())
        );

        mapa.parsujPoklady(suradnicePokladov.getText());

        HladacPokladov h = new HladacPokladov(Integer.parseInt(startX.getText()), Integer.parseInt(startY.getText()), mapa);

        GenetickyAlgoritmus g = new GenetickyAlgoritmus( mapa, h, Double.parseDouble(mutaciaField.getText()) );

        // Vypisanie riesenia
        Jedinec j = g.proces();
        VirtualnyStroj vm = new VirtualnyStroj(mapa, h);
        vm.setVypisRiesenie(true);
        vm.spustiProgram(j);

        riesenieFitness.setText(j.getFitness()+"");
        rieseniePoklady.setText(j.getPocetNajdenychPokladov()+"");
        riesenieKroky.setText(j.getPocetKrokov()+"");
        rieseniePohyb.setText("");
        int pohyby = j.getPohybySize();
        rieseniePohyb.appendText("Zacina na X=" + h.getStart().x + " , Y=" + h.getStart().y + " \n");
        for(int i=0; i<pohyby; i++){
            Point p = j.vyberPrvyPohyb();
            if(i != (pohyby-1)) {
                rieseniePohyb.appendText("hlada na X=" + p.x + " , Y=" + p.y + " \n");
            } else {
                rieseniePohyb.appendText("konci na X=" + p.x + " , Y=" + p.y + " \n");
            }
        }



    }

    @FXML
    void testujScenar(ActionEvent event) {

        Mapa mapa = new Mapa(
                Integer.parseInt(pocetPokladov.getText()),
                Integer.parseInt(mriezkaX.getText()),
                Integer.parseInt(mriezkaY.getText())
        );

        mapa.parsujPoklady(suradnicePokladov.getText());

        HladacPokladov h = new HladacPokladov(Integer.parseInt(startX.getText()), Integer.parseInt(startY.getText()), mapa);

        double priemerPoklady = 0;
        double priemerPocetKrokov = 0;

        for(int i=0; i<100; i++) {
            GenetickyAlgoritmus g = new GenetickyAlgoritmus(mapa, h, Double.parseDouble(mutaciaField.getText()));
            // Vypisanie riesenia
            Jedinec j = g.proces();
            priemerPocetKrokov += j.getPocetKrokov();
            priemerPoklady += j.getPocetNajdenychPokladov();
        }

        priemerPocetKrokov /= 100;
        priemerPoklady /= 100;

        System.out.println("Testovany scenar || PRIEMER  poklady: " +priemerPoklady + "   kroky " + priemerPocetKrokov);




    }

}
