/**
 * 
 */
package com.kyj.fx.nightmare;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * 
 */
public class TraySupport {

	public static boolean isTraySupport() {
		return SystemTray.isSupported();
	}

	public static void addAppToTray(Stage primaryStage) {
		if (!isTraySupport())
			return;

		Platform.setImplicitExit(false);
		try {
			// 트레이 아이콘 이미지 로드
			;
			URL imageURL = TraySupport.class.getClassLoader().getResource("images/Gargoyle.png"); // 트레이 아이콘 이미지 경로 설정
			Image trayIconImage = ImageIO.read(imageURL);

			// 트레이 아이콘 생성
			TrayIcon trayIcon = new TrayIcon(trayIconImage);
			trayIcon.setImageAutoSize(true);

			// 트레이 아이콘의 팝업 메뉴 설정
			PopupMenu popup = new PopupMenu();

			MenuItem openItem = new MenuItem("Open");
			//Open 메뉴 클릭시 활성화
			openItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Platform.runLater(() -> primaryStage.show());
				}
			});
			
			//블클릭시 활성화
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						Platform.runLater(() -> primaryStage.show());
					}
				}
			});
			popup.add(openItem);

			//종료버튼
			MenuItem exitItem = new MenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Platform.exit();
					System.exit(0);
				}
			});
			popup.add(exitItem);
			
			trayIcon.setToolTip("Nightmare");
			trayIcon.setPopupMenu(popup);

			// 시스템 트레이에 아이콘 추가

			SystemTray.getSystemTray().add(trayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
