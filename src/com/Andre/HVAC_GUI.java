package com.Andre;

import javax.swing.*;
import javax.swing.event.*;
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
    private JButton deleteTicketButton;

    DefaultListModel<ServiceCall> serviceCalltListModel;

    public HVAC_GUI() {
        super("List of HVAC Service Calls");
        setContentPane(rootPanel);
        //pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(1000, 500));

        //add the checkboxes to a button group so that
        //only one at a time can be selected
        final ButtonGroup topGroup = new ButtonGroup();
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
                } else if (waterHeaterCheckBox.isSelected()) {
                    String ageString = txtVar.getText();
                    int age = Integer.parseInt(ageString);
                    newCall = new WaterHeater(address, description, dateReported, age);
                } else {
                    String furnaceType = (String) typeComboBox.getSelectedItem();
                    if (furnaceType.equals("Forced Air")) {
                        type = 1;
                    } else if (furnaceType.equals("Boiler/Radiators")) {
                        type = 2;
                    } else {
                        type = 3;
                    }
                    newCall = new Furnace(address, description, dateReported, type);
                }
                HVAC_GUI.this.serviceCalltListModel.addElement(newCall);
                clearTxt();
                txtAddress.setText("");
                txtDescription.setText("");
                txtVar.setText("");


            }
        });

        resolveServiceCallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if a ticket is selected to be resolved
                if (!HVAC_GUI.this.serviceList.isSelectionEmpty()) {
                    ServiceCall toDelete = (ServiceCall) HVAC_GUI.this.serviceList.getSelectedValue();

                    Date dateResolved = new Date(); //Default constructor creates date with current date/time

                    //open input dialog to resolve the ticket
                    String s = JOptionPane.showInputDialog(
                            null,
                            "How did you resolve the issue:",
                            "Resolve Service Call",
                            JOptionPane.PLAIN_MESSAGE);
                    //It is a little strange to use a loop here but I couldn't fix
                    //of a way to validate my input window. Plus had some issues when I tried.
                    while (true) {
                        try {
                            String f = JOptionPane.showInputDialog(
                                    null,
                                    "How much was it?",
                                    "Resolve Service Call",
                                    JOptionPane.PLAIN_MESSAGE);
                            Double fee = Double.parseDouble(f);
                            toDelete.setFee(fee);
                            break;
                        } catch (NumberFormatException nfe) {
                            continue;
                        }
                    }


                    //If a string was returned, set resolution and date, and delete ticket from list
                    //if not ignore it
                    if ((s != null) && (s.length() > 0)) {
                        txtResolution.setText(s);
                        toDelete.setResolvedDate(dateResolved);
                        toDelete.setResolution(txtResolution.getText());
                        clearTxt();
                        txtAddress.setText("");
                        txtDescription.setText("");
                        txtVar.setText("");
                        return;
                    }
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
            public void valueChanged(ListSelectionEvent e) {
                //while working with selected calls, uncheck Check boxes
                topGroup.clearSelection();
                //Create Service call object to display all general fields
                ServiceCall selected = (ServiceCall) HVAC_GUI.this.serviceList.getSelectedValue();
                //if combobox is open have textfield show
                txtVar.setVisible(true);
                typeComboBox.setVisible(false);

                //Set the textfield according to the individual ticket
                if (HVAC_GUI.this.serviceList.getSelectedValue().getClass() == Furnace.class) {
                    Furnace f = (Furnace) HVAC_GUI.this.serviceList.getSelectedValue();
                    lblVar.setText("Type:");
                    Integer age = f.getFurnaceType();
                    String ageString;
                    if (age == 1) {
                        ageString = "Forced Air";
                    } else if (age == 2) {
                        ageString = "Boiler/Radiators";
                    } else {
                        ageString = "Older 'Octopus' Style";
                    }

                    txtVar.setText(ageString);
                } else if (HVAC_GUI.this.serviceList.getSelectedValue().getClass() == WaterHeater.class) {
                    WaterHeater w = (WaterHeater) HVAC_GUI.this.serviceList.getSelectedValue();
                    lblVar.setText("Age:");
                    txtVar.setText(Integer.toString(w.getWaterHeaterAge()));
                } else {
                    CentralAC ac = (CentralAC) HVAC_GUI.this.serviceList.getSelectedValue();
                    lblVar.setText("Model:");
                    txtVar.setText(ac.getModel());
                }

                try {
                    txtAddress.setText(selected.getServiceAddress());
                    txtDescription.setText(selected.getProblemDescription());
                    txtReportDate.setText(selected.getReportedDate().toString());

                    try {
                        txtResolution.setText(selected.getResolution());
                        txtResolveDate.setText(selected.getResolvedDate().toString());
                        txtFee.setText(Double.toString(selected.getFee()));
                    } catch (NullPointerException nfe) { //if the issue is not resolved yet
                        txtResolution.setText("Unresolved");
                        txtResolveDate.setText("Unresolved");
                        txtFee.setText("Unresolved");
                    }
                } catch (NullPointerException nfe2) {
                    return;
                }
            }
        });

        //if issue or reporter are changed remove all other entrees
        DocumentListener docListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                clearTxt();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                clearTxt();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //don't need it but needs to be declared
            }
        };
        txtAddress.getDocument().addDocumentListener(docListener);
        txtDescription.getDocument().addDocumentListener(docListener);
        txtVar.getDocument().addDocumentListener(docListener);

        //I just addet this to leave the resolved tickets in the list
        //to display that everything works. This is why it does not have much validation.
        //I also found an Issue that it does not update fast enough. and throes an error
        //although it still works all. But like I said is just for display reasons.
        deleteTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!HVAC_GUI.this.serviceList.isSelectionEmpty()) {
                    Object toDelete = HVAC_GUI.this.serviceList.getSelectedValue();
                    HVAC_GUI.this.serviceCalltListModel.removeElement(toDelete);

                }
                //show error message if no ticket is selected
                else {
                    JOptionPane.showMessageDialog(null,
                            "Please select a ticket to be deleted!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    //Method to clear the textboxes
    public void clearTxt() {
        txtReportDate.setText("");
        txtFee.setText("");
        txtResolution.setText("");
        txtResolveDate.setText("");
    }

}
