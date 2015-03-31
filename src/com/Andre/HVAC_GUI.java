package com.Andre;

//TODO: figure out scroll pane issue

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by Andre on 3/30/2015.
 */
public class HVAC_GUI extends JFrame {
    private JPanel rootPanel;
    private JTextField txtAddress;
    private JButton addServiceCallButton;
    private JButton resolveServiceCallButton;
    private JList serviceList;
    private JLabel lblVar;
    private JTextField txtVar;
    private JTextField txtDescription;
    private JTextField txtReportDate;
    private JTextField txtResolveDate;
    private JTextField txtResolution;
    private JTextField txtFee;
    private JCheckBox furnaceCheckBox;
    private JCheckBox centralACCheckBox;
    private JCheckBox waterHeaterCheckBox;
    private JComboBox typeComboBox;
    private JScrollPane scrollPanel;

    DefaultListModel<ServiceCall> serviceCalltListModel;

    public HVAC_GUI() {
        super("List of HVAC Service Calls");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(1000, 500));

        //add the checkboxes to a button group so that
        //only one at a time can be selected
        ButtonGroup topGroup = new ButtonGroup();
        topGroup.add(centralACCheckBox);
        topGroup.add(furnaceCheckBox);
        topGroup.add(waterHeaterCheckBox);

        serviceCalltListModel = new DefaultListModel<ServiceCall>();
        serviceList.setModel(serviceCalltListModel);
        serviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        typeComboBox.setVisible(false);

        typeComboBox.addItem("Forced Air");
        typeComboBox.addItem("Boiler/Radiators");
        typeComboBox.addItem("Older 'Octopus' Style");




        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (centralACCheckBox.isSelected()) {
                    lblVar.setText("Model:");
                    txtVar.setVisible(true);
                    typeComboBox.setVisible(false);
                }
                if (waterHeaterCheckBox.isSelected()) {
                    lblVar.setText("Age:");
                    txtVar.setVisible(true);
                    typeComboBox.setVisible(false);
                }
                if (furnaceCheckBox.isSelected()) {
                    lblVar.setText("Type:");
                    txtVar.setVisible(false);
                    typeComboBox.setVisible(true);

                }
            }
        };
        furnaceCheckBox.addChangeListener(listener);
        centralACCheckBox.addChangeListener(listener);
        waterHeaterCheckBox.addChangeListener(listener);


        addServiceCallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int type;
                Date dateReported = new Date(); //Default constructor creates date with current date/time
                String address = txtAddress.getText();
                String description = txtDescription.getText();
                ServiceCall newCall;
                if (centralACCheckBox.isSelected()) {
                    String model = txtVar.getText();
                    newCall = new CentralAC(address, description, dateReported, model);
                }

                else if (waterHeaterCheckBox.isSelected()) {
                    String ageString = txtVar.getText();
                    int age = Integer.parseInt(ageString);
                    newCall = new WaterHeater(address, description, dateReported, age);
                }

                else {
                    String furnaceType = (String)typeComboBox.getSelectedItem();
                    if (furnaceType.equals("Forced Air")) {
                        type = 1;
                    }
                    else if (furnaceType.equals("Boiler/Radiators")) {
                        type = 2;
                    }
                    else {
                        type = 3;
                    }
                    newCall = new Furnace(address, description, dateReported, type);
                }
                HVAC_GUI.this.serviceCalltListModel.addElement(newCall);

            }
        });

        resolveServiceCallButton.addActionListener(new ActionListener() {
            @Override
            //TODO: NOT working yet
            //Can't recognice specific class
            public void actionPerformed(ActionEvent e) {
                //if a ticket is selected to be resolved
                if (!HVAC_GUI.this.serviceList.isSelectionEmpty()) {
                   //ServiceCall toDelete = HVAC_GUI.this.serviceList.getSelectedValues();

                    Date dateResolved = new Date(); //Default constructor creates date with current date/time

                    //open input dialog to resolve the ticket
                    String s = JOptionPane.showInputDialog(
                            null,
                            "How did you resolve the issue:",
                            "Resolve Service Call",
                            JOptionPane.PLAIN_MESSAGE);

                    /*//If a string was returned, set resolution and date, and delete ticket from list
                    //if not ignore it
                    if ((s != null) && (s.length() > 0)) {
                        txtResolution.setText(s);
                        toDelete.setResolvedDate(dateResolved);
                        toDelete.setResolution(txtResolved.getText());
                        HVAC_GUI.this.serviceCalltListModel.removeElement(toDelete);
                        clearTxt();
                        txtIssue.setText("");
                        txtReportedBy.setText("");
                        return;
                    }*/
                }
                //show error message if no ticket is selected
                else {
                    JOptionPane.showMessageDialog(null,
                            "Please select a ticket to be resolved!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        serviceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            //TODO: same as resolved
            public void valueChanged(ListSelectionEvent e) {
                //if Tickets are deleted.
                /*try {
                    txtAddress.setText(HVAC_GUI.this.serviceList.getSelectedValue().
                    txtDescription.setText(HVAC_GUI.this.serviceList.getSelectedValue().getClass().//;
                    txtReportDate.setText(HVAC_GUI.this.serviceList.getSelectedValue().getDateReported().toString());
                    txtVar.setText(HVAC_GUI.this.serviceList.getSelectedValue().getTicketID() + ""); //little hack to trick it into taking the int as String
                    try {
                        txtResolution.setText(HVAC_GUI.this.serviceList.getSelectedValue().getResolution());
                        txtResolveDate.setText(HVAC_GUI.this.serviceList.getSelectedValue().getResolvedDate().toString());
                    } catch (NullPointerException nfe) { //if the issue is not resolved yet
                        txtResolution.setText("Unresolved");
                        txtResolution.setText("Unresolved");
                    }
                } catch (NullPointerException nfe2) {
                    return;
                }*/
            }
        });
    }
}
