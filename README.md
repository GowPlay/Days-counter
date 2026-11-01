# Days Counter (Fabric, Minecraft 26.2)

Muestra en la parte superior de la pantalla cuántos días han pasado en el mundo.

## Requisitos
- Minecraft 26.2 + Fabric Loader 0.19+ + Fabric API 0.161.0+26.2
- Java 25 (para compilar y para jugar)

## Compilar
    ./gradlew build        (Windows: gradlew.bat build)

El .jar queda en build/libs/ (usa el que NO termina en -sources). Cópialo a .minecraft/mods junto con Fabric API.

## Controles (se pueden cambiar en Opciones > Controles > Days Counter)
- J: cambiar posición (izquierda / centro / derecha)
- K: cambiar tipografía
- H: mostrar / ocultar

## Configuración: config/dayscounter.json
position (LEFT, CENTER, RIGHT), font (DEFAULT, UNIFORM, ENCHANTING, ILLAGER, PIXEL, CREEPY, COMIC, CUSTOM),
format (ej. "Día %d"), startAtOne, shadow, bold, color (hex), scale, marginX, marginY.

## Tipografía personalizada
Elige font = CUSTOM y crea un resource pack que sobrescriba assets/dayscounter/font/custom.json
usando un proveedor "ttf" con tu propia fuente.

## Licencias de fuentes
Press Start 2P, Creepster y Bangers: SIL Open Font License 1.1 (Google Fonts).
