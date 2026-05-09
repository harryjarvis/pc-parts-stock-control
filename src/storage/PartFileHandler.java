package storage;

import java.io.*;
import part.*;
import main.Extras;

/**
 * Student ID: 21360548
 * Handles loading and saving {@link Part} data to and from persistent storage.
 * Parts are stored in a comma-separated format where each line represents
 * a single part. The first value is the part type (e.g. CPU, GPU), which is
 * used to reconstruct the correct subclass when loading.

 */
public class PartFileHandler {

    /**
     * Loads parts from a file into memory.
     * Each line contains a part type followed by the common fields (ID, name,
     * manufacturer, price) and then type-specific fields depending on the subclass.
     * The loaded part is added using {@link PartMethods#addPartFromLoad(Part)} so
     * that existing IDs are preserved and the internal ID counter can be updated.
     * @param filePath path to the part data file
     * @param pm       part logic handler used to store loaded parts
     */
    public void loadParts(String filePath, PartMethods pm) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                // Split CSV line into fields
                String[] data = line.split(",");

                // Remove BOM if present
                data[0] = data[0].replace("\uFEFF", "").trim();

                // First column = type
                String type = data[0].trim();

                int id = Integer.parseInt(data[1].trim());
                String name = data[2].trim();
                String manufacturer = data[3].trim();
                double price = Double.parseDouble(data[4].trim());

                Part part = switch (type) {

                    case "CPU" -> {
                        String socket = data[5].trim();
                        int cores = Integer.parseInt(data[6].trim());
                        int threads = Integer.parseInt(data[7].trim());
                        double baseClock = Double.parseDouble(data[8].trim());
                        double boostClock = Double.parseDouble(data[9].trim());

                        yield new CPU(id, name, manufacturer, price,
                                socket, cores, threads, baseClock, boostClock);
                    }

                    case "GPU" -> {
                        String chipset = data[5].trim();
                        String vram = data[6].trim();
                        double baseClock = Double.parseDouble(data[7].trim());
                        double boostClock = Double.parseDouble(data[8].trim());
                        int length = Integer.parseInt(data[9].trim());
                        String colour = data[10].trim();

                        yield new GPU(id, name, manufacturer, price,
                                chipset, vram, baseClock, boostClock,
                                length, colour);
                    }

                    case "Motherboard" -> {
                        String socket = data[5].trim();
                        String formFactor = data[6].trim();
                        String chipset = data[7].trim();
                        int maxMemory = Integer.parseInt(data[8].trim());
                        boolean wifi = Boolean.parseBoolean(data[9].trim());
                        String colour = data[10].trim();

                        yield new Motherboard(id, name, manufacturer, price,
                                socket, formFactor, chipset,
                                maxMemory, wifi, colour);
                    }

                    case "Case" -> {
                        String caseType = data[5].trim();
                        int maxGpuLength = Integer.parseInt(data[6].trim());
                        String colour = data[7].trim();
                        double length = Double.parseDouble(data[8].trim());
                        double width = Double.parseDouble(data[9].trim());
                        double height = Double.parseDouble(data[10].trim());
                        double volume = Double.parseDouble(data[11].trim());

                        yield new Case(id, name, manufacturer, price, caseType,
                                maxGpuLength, colour,
                                length, width, height, volume);
                    }

                    default -> {
                        System.out.println(
                                Extras.errorMsg + "Unknown part type in file: " + type + Extras.reset
                        );
                        yield null;
                    }
                };

                // Adds loaded part into memory (IDs preserved)
                pm.addPartFromLoad(part);
            }

        } catch (Exception e) {
            System.out.println(Extras.errorMsg + "Warning: Parts Not Loaded! " + e.getMessage() + Extras.reset);
            e.printStackTrace();
        }
    }

    /**
     * Saves all parts currently stored in memory to a file.
     * Each part is written as a single line in a comma-separated format.
     * The part type is written first to support correct subclass reconstruction
     * when loading.
     * @param filePath path to the output part data file
     * @param pm       part logic handler containing parts to save
     */
    public void saveParts(String filePath, PartMethods pm) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Part p : pm.viewAllParts()) {

                pw.print(p.getType() + "," +
                        p.getId() + "," +
                        p.getName() + "," +
                        p.getManufacturer() + "," +
                        p.getPrice());

                if (p instanceof CPU c) {
                    pw.println("," +
                            c.getSocket() + "," +
                            c.getCores() + "," +
                            c.getThreads() + "," +
                            c.getBaseClock() + "," +
                            c.getBoostClock()
                    );
                }

                else if (p instanceof GPU g) {
                    pw.println("," +
                            g.getChipset() + "," +
                            g.getVram() + "," +
                            g.getBaseClock() + "," +
                            g.getBoostClock() + "," +
                            g.getLength() + "," +
                            g.getColour()
                    );
                }

                else if (p instanceof Motherboard m) {
                    pw.println("," +
                            m.getSocket() + "," +
                            m.getFormFactor() + "," +
                            m.getChipset() + "," +
                            m.getMaxMemory() + "," +
                            m.isWifi() + "," +
                            m.getColour()
                    );
                }

                else if (p instanceof Case ca) {
                    pw.println("," +
                            ca.getType() + "," +
                            ca.getMaxGpuLength() + "," +
                            ca.getColour() + "," +
                            ca.getLength() + "," +
                            ca.getWidth() + "," +
                            ca.getHeight() + "," +
                            ca.getVolume()
                    );
                }
            }

            System.out.println(Extras.successMsg + "\nParts saved successfully.");

        } catch (Exception e) {
            System.out.println(Extras.errorMsg + "\nWarning: Parts Not Saved! " + e.getMessage() + Extras.reset);
            e.printStackTrace();
        }
    }
}
