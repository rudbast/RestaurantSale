/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantsale.Tools;

/**
 *
 * @author rudolf
 */
public class RequestHandler {

    public static String decodeMessage(String Message) {
        String result = "", query, TABLE_NO, NAME, NO;
        String[] line = Message.split(";"), parts;
        int TYPE, QTY;
        // message type handler
        switch (line[0]) {
            case "MNU": // REQUEST MENU LIST
                TYPE = 0;
                query = "SELECT `NAME`, `PRICE` FROM `Menu` WHERE TYPE = 'FOOD' ORDER BY `NAME`";
                result += DBHandler.getTable(query, TYPE) + ";";

                query = "SELECT `NAME`, `PRICE` FROM `Menu` WHERE TYPE = 'BEVERAGE' ORDER BY `NAME`";
                result += DBHandler.getTable(query, TYPE);
                break;

            case "ORD": // ORDERING
                syncedOrder(line);
                break;

            case "SRC": // SEARCH
                TYPE = 0;
                query = "SELECT `NAME`, `PRICE` FROM `Menu`";
                for(int i=1; i<line.length; ++i){
                    if(i == 1) query += " WHERE ";
                    else query += " AND ";
                    parts = line[i].split(":");
                    switch(parts[0]){
                        case "NAME":
                            query += "`NAME` LIKE '%" + parts[1] + "%'";
                            break;
                        case "CATEGORY":
                            query += "`TYPE` LIKE '%" + parts[1] + "%'";
                            break;
                        case "PRICE":
                            String[] inner = parts[1].split("\\.");
                            if(inner.length > 1)
                                query += "`PRICE` BETWEEN " + inner[0] + " AND " + inner[1];
                            else query += "`PRICE` " + inner[0];
                            break;
                    }
                }
                result = DBHandler.getTable(query, TYPE);
                break;

            case "QUE": // REQUEST QUEUE LIST
                TYPE = 1;
                query = "SELECT `NAME`, `TABLE_NO`, `QUANTITY` FROM `Queue` WHERE `STATUS` = 'QUEUE'";
                result = DBHandler.getTable(query, TYPE);
                break;

            case "CHK": // CHECK ORDERED
                TYPE = 1;
                TABLE_NO = line[1];
                query = "SELECT `NAME`, `TABLE_NO`, SUM(`QUANTITY`) FROM `Queue` WHERE `TABLE_NO` = '" + TABLE_NO + "' GROUP BY `TABLE_NO`,`NAME` ORDER BY `NAME`";
                result = DBHandler.getTable(query, TYPE);
                break;

            case "DNE": // ORDER IS READY
                TYPE = 2;
                TABLE_NO = line[1];
                NAME = line[2];
                QTY = Integer.parseInt(line[3]);
                NO = line[4];
                // insert specified order into sales
                query = "INSERT INTO `Sales`(`NO`, `NAME`, `TABLE_NO`, `QUANTITY`) VALUES(" + NO + ", '" + NAME + "', '" + TABLE_NO + "', " + QTY + ")";
                DBHandler.execNonQuery(query);

                // check existing order
                query = "SELECT `QUANTITY` FROM `Queue` WHERE `NAME` = '" + NAME + "' AND `TABLE_NO` = '" + TABLE_NO
                        + "' AND `STATUS` = 'DONE'";
                String temp = DBHandler.getField(query);
                if(!temp.equals("")){
                    // found existing order (DONE)
                    query = "UPDATE `Queue` SET `QUANTITY` = " + (Integer.parseInt(temp) + QTY) + "  WHERE `TABLE_NO` = '" + TABLE_NO
                            + "' AND `NAME` = '" + NAME + "' AND `STATUS` = 'DONE'";
                    DBHandler.execNonQuery(query);

                    // remove the specified order from db (quantity added into existing order)
                    query = "DELETE FROM `Queue` WHERE `NO` = " + NO;
                    DBHandler.execNonQuery(query);
                }
                else {
                    // no existing order found, update status to 'DONE'
                    query = "UPDATE `Queue` SET `STATUS` = 'DONE' WHERE `NO` = " + NO;
                    DBHandler.execNonQuery(query);
                }

                query = "SELECT `NAME`, `TABLE_NO`, `QUANTITY`, `NO` FROM `Queue` WHERE `STATUS` = 'QUEUE'";
                result = DBHandler.getTable(query, TYPE);
                break;

            case "CHD": // CHECK DONE
                TYPE = 1;
                query = "SELECT `NAME`, `TABLE_NO`, `QUANTITY` FROM `Sales` ORDER BY `NO` DESC LIMIT 10";
                result = DBHandler.getTable(query, TYPE);
                break;

            case "FIN": // CLEAR CURRENT TABLE (INITIATE NEW TABLE)
                TABLE_NO = line[1];
                query = "DELETE FROM `Queue` WHERE `TABLE_NO` = '" + TABLE_NO + "'";
                DBHandler.execNonQuery(query);
                break;

            case "SQUE":// REQUESTING QUEUE LIST FOR SERVER
                TYPE = 2;
                query = "SELECT `NAME`, `TABLE_NO`, `QUANTITY`, `NO` FROM `Queue` WHERE `STATUS` = 'QUEUE'";
                result = DBHandler.getTable(query, TYPE);
                break;
        }
//        System.out.println(result);
        return result;
    }

    public static synchronized void syncedOrder(String[] line) {
        int QTY;
        String query, TABLE_NO, NAME;
        String[] parts;

        TABLE_NO = line[1];
        for(int i=2; i<line.length; ++i){
            parts = line[i].split("\\.");
            QTY = Integer.parseInt(parts[0]);
            NAME = parts[1];

            query = "INSERT INTO `Queue`(`NAME`, `TABLE_NO`, `QUANTITY`) VALUES('" + NAME + "', '" + TABLE_NO + "', " + QTY + ")";
            DBHandler.execNonQuery(query);
        }
    }
}
