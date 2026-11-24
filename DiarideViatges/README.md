# 🏔️ Diari de Viatges / Crònica de Ruta 🗺️

Aplicació Android desenvolupada amb **Jetpack Compose** i **Arquitectura MVVM** (Model-View-ViewModel) per registrar, gestionar i exportar rutes o escapades de viatges.

---

## 🏗️ Arquitectura del Projecte (MVVM)

L'aplicació està estructurada seguint el patró d'arquitectura **MVVM** i separació de responsabilitats recomanat per Android:

```text
org.proven.diarideviatges
 ├── model
 │    ├── RouteDifficulty.kt      # Enum de dificultats (Fàcil, Moderada, Exigent)
 │    ├── RouteEntry.kt           # Data class immutable per a una ruta
 │    └── TravelDiaryUiState.kt   # Estat immutble de la UI (camps, errors, històric)
 ├── data
 │    └── StorageManager.kt       # Gestor de persistència interna JSON a filesDir
 ├── viewmodel
 │    └── TravelDiaryViewModel.kt # ViewModel amb MutableStateFlow i lògica de negoci
 └── ui
      ├── screens
      │    ├── FormScreen.kt      # Pantalla 1: Formulari de nova ruta amb validació
      │    ├── PostalScreen.kt    # Pantalla 2: Targeta visual de viatge (Postal) + Exportació
      │    └── HistoryScreen.kt   # Històric de rutes guardades
      └── navigation
           └── AppNavigation.kt   # Graella de navegació amb Navigation Compose
```

---

## 📱 Vistes de l'Aplicació

1. **Formulari de Nova Ruta (`FormScreen`)**:
   - Camps obligatoris amb validació en temps real (`OutlinedTextField` per Nom i Destí).
   - Selecció de dificultat mitjançant `FilterChip` (Fàcil, Moderada, Exigent).
   - Introducció de pressupost numèric amb teclat decimal (`KeyboardType.Decimal`).
   - Selector de data interactiu de Material 3 (`DatePickerDialog` & `DatePicker`).
   - Comutador `Switch` per indicar si la ruta ha estat en solitari.
   - Camp multilínia per descriure les notes i el relat de la jornada.
   - Botó d'acció principal "Generar Postal / Crònica" (activat només si el formulari és vàlid).

2. **Targeta Visual de Viatge (`PostalScreen`)**:
   - Maquetació en format postal amb el destí destacat, nom de la ruta, data, insígnies de dificultat i pressupost, indicador de solitari/companyia i el relat.
   - Botó **Exportar Crònica (.md / SAF)** per desar el fitxer Markdown a l'emmagatzematge extern de l'usuari.
   - Botó **Compartir** utilitzant l'Android Intent `Intent.ACTION_SEND` per enviar el relat a WhatsApp, correu o altres aplicacions.

3. **Històric de Rutes (`HistoryScreen`)**:
   - Llista de totes les rutes desades a l'emmagatzematge privat `filesDir`.
   - Permet seleccionar qualsevol ruta per veure la seva postal o eliminar rutes de l'històric.

---

## 💾 Comparativa Tècnica d'Emmagatzematge

| Característica | Emmagatzematge Intern (`context.filesDir`) | Emmagatzematge Extern (`Storage Access Framework` - SAF) |
| :--- | :--- | :--- |
| **Ubicació** | `/data/data/org.proven.diarideviatges/files/diari_viatges.json` | Carpeta triada per l'usuari (Documents, Descàrregues, etc.) |
| **Privacitat** | 100% Privat. Només l'aplicació hi pot accedir. | Públic / Compartible. Accessible per l'usuari i altres apps. |
| **Permisos** | No requereix cap permís d'emmagatzematge. | Utilitza el selector natiu `CreateDocument` sense requerir permisos perillosos. |
| **Format utilitzat** | Fitxer JSON (`Gson`) | Fitxer de text/Markdown (`.md` / `.txt`) |
| **Què passa si es desinstal·la l'app?** | ⚠️ **Es borra completament** automàticament per Android. | ✅ **Es manté intacte** a la memòria del dispositiu per sempre. |

---

## 🎓 Conceptes i Tecnologies Apreses

- **StateFlow i Immutabilitat**: Utilització de `MutableStateFlow` privat i `StateFlow` públic amb `.asStateFlow()`, actualitzant l'estat utilitzant `.copy()` per garantir la reactivitat sense efectes secundaris.
- **`FilterChip` (Material 3)**: Selector d'opció única per a la dificultat de la ruta amb icona d'activació.
- **`DatePickerDialog` & `DatePicker` (Material 3)**: Selector natiu de dates de Jetpack Compose.
- **Storage Access Framework (`ActivityResultContracts.CreateDocument`)**: Selector de fitxers del sistema per exportar contingut sense sol·licitar permisos WRITE_EXTERNAL_STORAGE.
- **Android Intents (`Intent.ACTION_SEND`)**: Compartició de text mitjançant la fulla d'accions del sistema (`Intent.createChooser`).
- **Serialització JSON amb Gson**: Conversió de llistes d'objectes `RouteEntry` a text JSON per a la persistència en disc.
- **Navigation Compose**: Definició de rutes de navegació entre pantalles i pas de l'estat seleccionat.

---

## 🛠️ Com Executar el Projecte

1. Obre el projecte a **Android Studio**.
2. Fes un **Gradle Sync**.
3. Executa l'aplicació en un emulador o dispositiu físic Android (API 24 o superior).
4. Executa els tests unitaris des del terminal de Gradle:
   ```bash
   ./gradlew testDebugUnitTest
   ```
