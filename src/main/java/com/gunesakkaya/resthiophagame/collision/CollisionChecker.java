//package com.gunesakkaya.resthiophagame.collision;
//
//import com.gunesakkaya.resthiophagame.main.GamePanel;
//import com.gunesakkaya.resthiophagame.main.GameObject;
//
//public class CollisionChecker {
//
//    GamePanel gp;
//
//    public CollisionChecker(GamePanel gp) {
//        this.gp = gp;
//    }
//
//    public void checkTile(GameObject obj) {
//        int leftWorldX = obj.worldX + obj.solidArea.x;
//        int rightWorldX = obj.worldX + obj.solidArea.x + obj.solidArea.width;
//        int topWorldY = obj.worldY + obj.solidArea.y;
//        int bottomWorldY = obj.worldY + obj.solidArea.y + obj.solidArea.height;
//
//        int leftCol = leftWorldX / gp.tileSize;
//        int rightCol = rightWorldX / gp.tileSize;
//        int topRow = topWorldY / gp.tileSize;
//        int bottomRow = bottomWorldY / gp.tileSize;
//
//        int tileNum1, tileNum2;
//
//        switch (obj.direction) {
//            case "up":
//                topRow = (topWorldY - obj.speed) / gp.tileSize;
//                tileNum1 = gp.tileM.mapTileNum[topRow][leftCol];
//                tileNum2 = gp.tileM.mapTileNum[topRow][rightCol];
//                break;
//            case "down":
//                bottomRow = (bottomWorldY + obj.speed) / gp.tileSize;
//                tileNum1 = gp.tileM.mapTileNum[bottomRow][leftCol];
//                tileNum2 = gp.tileM.mapTileNum[bottomRow][rightCol];
//                break;
//            case "left":
//                leftCol = (leftWorldX - obj.speed) / gp.tileSize;
//                tileNum1 = gp.tileM.mapTileNum[topRow][leftCol];
//                tileNum2 = gp.tileM.mapTileNum[bottomRow][leftCol];
//                break;
//            case "right":
//                rightCol = (rightWorldX + obj.speed) / gp.tileSize;
//                tileNum1 = gp.tileM.mapTileNum[topRow][rightCol];
//                tileNum2 = gp.tileM.mapTileNum[bottomRow][rightCol];
//                break;
//            default:
//                return;
//        }
//
//        // Eğer herhangi bir tile çarpışmalıysa:
//        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
//            obj.collisionOn = true;
//        } else {
//            obj.collisionOn = false;
//        }
//    }
//}
