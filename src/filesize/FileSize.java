package filesize;


import java.io.File;
import java.util.*;


class FileSize {
    private HashMap<String, Long> result = new HashMap<>();
    private File baseDirectory = new File("C:\\Users\\User\\Desktop");
    private Integer baseByte = 1024;
    private Scanner sc = new Scanner(System.in);


    /**
     * Display all available function .
     */
    private void displayAvailableFun() {
        System.out.println("TYPE");
        System.out.println("-h : to see files with sizes");
        System.out.println("-c : sum of sizes");
        System.out.println("--si : to take 1000 as units of measurement");
    }

    /**
     * This fun convert file size to kiloByte , megaByte..
     * baseByte define the conversion base { 1 KB = 1000 B or 1 KB = 1024 B}
     */
    private String converter(Long kiloByte, Integer baseByte) {
        double bytes = kiloByte.doubleValue() * baseByte;
        double megaB = kiloByte.doubleValue() / baseByte;
        double gigaB = kiloByte / Math.pow(kiloByte.doubleValue(), 2.0);
        if (bytes < 1000) return bytes + " B";
        else if (gigaB > 0.9) return gigaB + " GB";
        else if (megaB > 0.9) return megaB + " MB";
        else return kiloByte + " KB";
    }

    private ArrayList<File> searchFile(String name, File baseDirectory) {
        ArrayList<File> result = new ArrayList<>();
        if (!baseDirectory.isDirectory()) {
            if (baseDirectory.isFile())
                result.add(baseDirectory);
        } else {
            for (File temp : Objects.requireNonNull(baseDirectory.listFiles())) {
                if (temp.getName().equals(name)) {
                    result.add(temp.getAbsoluteFile());
                    if (!result.isEmpty())
                        break;
                } else if (temp.isDirectory()) {
                    ArrayList<File> answr = searchFile(name, temp);

                    if (!answr.isEmpty())

                        result.add(answr.get(0));
                }
            }
        }
        return result;
    }

    private Long getSize(File directory) {

        long length = 0;
        if (!directory.isDirectory()) {
            length = directory.length();
        } else if (directory.isDirectory()) {

            for (File files : Objects.requireNonNull(directory.listFiles())) {
                if (files.isFile())
                    length += files.length();
                else
                    getSize(files);
            }
        }
        return length;
    }

    private Boolean use(String name) {

        if (searchFile(name, baseDirectory).isEmpty())
            return false;
        File paths = searchFile(name, baseDirectory).get(0).getAbsoluteFile();
        result.put(name, getSize(paths) / 1024);
        return true;
    }

    private void display(int nbMenu) {
        switch (nbMenu) {
            case 1:
                System.out.println("Put the names of files and Type DONE when all files done!");
                break;
            case 2:
                System.out.println("Put the directory");
                break;
            default:
                System.err.println("You did not choose from the choices offered");
                System.out.println("Please retry");
                break;
        }
    }

    void runMenu() {
        System.out.println("Welcome to FileFolderSearcher\n" +
                "the currently file is:" + baseDirectory);
        System.out.println("Press");
        System.out.println("1 - to find in this directory");
        System.out.println("2 - to change directory");
        System.out.println("-0 - at any time to exit of the system");
        int i;
        String input;
        do {
            input = sc.next();
            try {
                i = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                i = -1;
            }
            if (i == -0 )
                break;
            this.display(i);
            String nameFile;
             switch (i) {
                 case 1:
                     boolean fileExist;
                     do {
                         nameFile = sc.next();
                         if (nameFile.equals("-0"))
                            break;
                         fileExist = use(nameFile);
                         if (nameFile.equals("DONE"))
                             break;
                         if (!fileExist) {
                             System.err.println("this file can't found; put the correct file or type 0 to exit!");
                             System.out.println("type DONE when all done!");
                         }
                     } while (!nameFile.equals("DONE"));
                     if (result.isEmpty() || nameFile.equals("-0")) {
                         if (result.isEmpty()) System.err.println("you did not type any file");
                         break;
                     } else

                     this.displayAvailableFun();
                     String j;
                     boolean comandAlready = false;
                     String size;
                     do {

                         j = sc.next();
                         if (j.equals("-0"))
                             break;
                         switch (j) {
                             case "-h":
                                 comandAlready = true;
                                 for (Map.Entry<String, Long> entry : result.entrySet()) {
                                     nameFile = entry.getKey();
                                     size = converter(entry.getValue(), baseByte);
                                     System.out.println(nameFile + " : " + size);
                                 }
                                 break;
                             case "-c":
                                 nameFile = Arrays.toString(result.keySet().toArray());
                                 List<Long> resultValue = new ArrayList<>(result.values());
                                 long sum = 0;
                                 for (Long el : resultValue)
                                     sum += el;
                                 if (comandAlready) {
                                     size = converter(sum, baseByte);
                                     System.out.println("sum of sizes : " + nameFile + " = " + size);
                                 } else {
                                     size = sum + "";
                                     System.out.println("sum of sizes : " + nameFile + " = " + size + " KB");
                                 }
                                 break;
                             case "--si":
                                 this.baseByte = 1000;
                                 break;
                             default:
                                 System.err.println("Please choose command proposed");
                                 displayAvailableFun();
                         }
                     } while (!j.equals("-0"));
                     break;
                 case 2:
                     boolean fileCanRead = true;
                     do {
                         input = sc.next();
                         if (input.equals("-0"))
                            break;
                         if (new File(input).canRead())
                             this.baseDirectory = new  File(input);
                         else {
                             System.err.println("this directory can not read\n" +
                                     "Please retry or type 0 to exit");
                             fileCanRead = false;
                         }
                     } while (!fileCanRead);
                     if (input.equals("-0"))
                         break;
                         runMenu();
             }


        } while (i < 0 || i > 3);

    }

}

class Main {

    public static void main(String[] args) {
        FileSize x = new FileSize();
        x.runMenu();
    }
}