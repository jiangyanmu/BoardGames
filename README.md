# BoardGames (桌遊合集)

這是一個使用 Java Swing 開發的桌遊合集專案，包含了經典的 **井字棋 (Tic-Tac-Toe)** 與 **黑白棋 (Reversi/Othello)**。

## 功能特色

*   **遊戲選擇主選單**：啟動後可透過圖形介面選擇想遊玩的遊戲。
*   **井字棋 (Tic-Tac-Toe)**：經典的 3x3 連線遊戲，包含 AI 對戰功能。
*   **黑白棋 (Reversi)**：經典的 8x8 翻轉棋類遊戲，包含 AI 對戰功能。
*   **現代化介面**：使用簡潔明亮的配色風格，提供良好的使用者體驗。

## 專案結構

專案採用標準的 Java package 結構，原始碼位於 `src` 目錄下：

```
.
└── src/
    └── com/
        └── boardgames/
            ├── Main.java              // 程式進入點，負責啟動遊戲選擇視窗
            ├── GameSelectionGUI.java  // 遊戲選擇介面
            ├── common/                // 共用類別 (如 AI 介面)
            ├── reversi/               // 黑白棋相關類別 (遊戲邏輯、GUI、AI)
            └── tictactoe/             // 井字棋相關類別 (遊戲邏輯、GUI、AI)
```

## 如何執行

請確保您的電腦已安裝 Java Development Kit (JDK) 8 或以上版本。

### 1. 編譯程式碼

在專案根目錄 (`BoardGames/`) 開啟終端機 (Terminal) 或命令提示字元 (CMD)。

首先，建立存放編譯檔的目錄 (如果不存在)：
```bash
mkdir bin
```

接著，編譯所有 Java 檔案。

**Windows (PowerShell):**
```powershell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } > sources.txt
javac -d bin @sources.txt
del sources.txt
```

**Mac / Linux / Git Bash:**
```bash
javac -d bin $(find src -name "*.java")
```

### 2. 執行程式

編譯完成後，執行以下指令啟動遊戲：

```bash
java -cp bin com.boardgames.Main
```

## 開發資訊

*   **程式語言**: Java
*   **GUI 框架**: Java Swing
*   **編碼**: UTF-8