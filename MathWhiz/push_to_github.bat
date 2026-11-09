@echo off
title Push MathWhiz to GitHub
cls
echo =======================================================================
echo          Push BAPPI - MATH WHIZ to GitHub (Bappi-_Whiz_Game)
echo =======================================================================
echo.
echo Target Repo: https://github.com/Bappi003/Bappi-_Whiz_Game
echo.
echo Choose your push method:
echo   [1] Normal Push (GitHub Browser Login for Bappi003)
echo   [2] GitHub Token (If you have a Personal Access Token / PAT)
echo.
set /p CHOICE="Enter 1 or 2: "

cd /d "D:\Agri AI App\MathWhiz"
git add .
git commit -m "Complete BAPPI - MATH WHIZ Project" 2>nul

if "%CHOICE%"=="2" (
    echo.
    set /p GH_TOKEN="Paste your GitHub Token (e.g. ghp_...): "
    if not "%GH_TOKEN%"=="" (
        git remote set-url origin https://%GH_TOKEN%@github.com/Bappi003/Bappi-_Whiz_Game.git
    )
) else (
    git remote set-url origin https://Bappi003@github.com/Bappi003/Bappi-_Whiz_Game.git
)

echo.
echo [*] Pushing files to GitHub...
git push -u origin main

echo.
if %errorlevel% equ 0 (
    echo =======================================================================
    echo  [SUCCESS] All files successfully uploaded to:
    echo  https://github.com/Bappi003/Bappi-_Whiz_Game
    echo =======================================================================
) else (
    echo.
    echo =======================================================================
    echo  [NOTICE] Push failed. 
    echo  Likely reason: Your computer is logged in as 'Bappi360', 
    echo  while the new repo belongs to 'Bappi003'.
    echo.
    echo  FIX:
    echo  1. On GitHub (Bappi-_Whiz_Game) -> Settings -> Collaborators
    echo     -> Add 'Bappi360' as collaborator.
    echo  OR
    echo  2. Generate a Token on Bappi003 -> Settings -> Developer Settings 
    echo     -> Personal Access Tokens -> Tokens (classic) with 'repo' scope.
    echo =======================================================================
)
echo.
pause
