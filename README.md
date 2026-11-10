# 1\. Anàlisi i documentació de l'arquitectura MVVM

## 1.1. Documentació dels components

**Model:**

El Model és on es guarden les dades de l'aplicació. A `MVVMSimple.kt` és la classe `SimpleState`, que guarda el comptador i el missatge que es mostra a la pantalla.

**ViewModel:**

El ViewModel s'encarrega de gestionar les dades i les accions de l'aplicació. A `MVVMSimple.kt` és `SimpleViewModel`, que permet incrementar el comptador i reiniciar-lo. També comunica els canvis a la View mitjançant l'estat.

**View (UI / Compose):**

És la part visual de l'aplicació. En aquest exemple són `MainActivity` i `SimpleScreen`. Mostra el missatge i els botons. Rep els canvis consultant l'estat amb `.value` i avisa el ViewModel quan l'usuari prem un botó.

## 1.2. Anàlisi comparativa (MVVMSimple vs MVVM-separatModel)

**Estructura i separació de responsabilitats:**

A `MVVMSimple`, el Model (`SimpleState`) només guarda les dades i la lògica està principalment al ViewModel.

En canvi, a `MVVM-separatModel`, el Model està separat en dues classes: `CardModel` i `BoardModel`. Aquestes també tenen la seva pròpia lògica.

A `MVVMSimple`, la lògica d'incrementar i reiniciar el comptador està al ViewModel. A `MVVM-separatModel`, la lògica de girar les cartes i comprovar les parelles està al Model.

**Immutabilitat i gestió de l'estat:**

A `MVVM-separatModel`, les dades es modifiquen creant nous objectes amb el mètode `.copy()`.

Aquest mètode permet copiar un objecte canviant només les dades necessàries, sense modificar l'original. Això ajuda a detectar els canvis d'estat i actualitzar la interfície.

**Avantatges de la separació:**

Separar el Model del ViewModel fa que el codi estigui més ordenat i sigui més fàcil d'entendre i modificar. També permet reutilitzar la lògica del joc i provar-la sense dependre de la interfície.

## 1.3. Anàlisi comparativa: StateFlow / MutableStateFlow i mutableStateOf / State

**StateFlow i MutableStateFlow:**

`StateFlow` serveix per observar els canvis d'un estat. `MutableStateFlow` permet modificar aquest estat.

**mutableStateOf i State:**

`mutableStateOf` crea un estat que Compose pot observar. `State` permet consultar aquest estat sense modificar-lo directament.

**Diferències:**

La principal diferència és que `StateFlow` pertany a Kotlin Coroutines, mentre que `mutableStateOf` forma part de Jetpack Compose.

Amb `StateFlow` utilitzem `collectAsState()` per observar els canvis. Amb `mutableStateOf` podem consultar directament el valor amb `.value`.

**Quin escolliria i per què?**

Per al Tres en Ratlla escolliria `MutableStateFlow` i `StateFlow`, perquè permeten gestionar l'estat de manera ordenada i separar la part que modifica les dades de la que només les consulta. A més, és el sistema que demana l'enunciat.




# 2\. Arquitectura utilitzada en el projecte Tres en Ratlla

En aquest projecte he desenvolupat el joc Tres en Ratlla utilitzant l'arquitectura MVVM amb un Model separat, seguint l'estructura del fitxer `MVVM-separatModel`.

He organitzat el codi en tres carpetes principals: `model`, `viewmodel` i `view`.

## 2.1. Model

Dins de la carpeta `model` hi ha dos fitxers:

**CellModel.kt**

Representa cada casella del tauler. Guarda el seu identificador (`id`), la seva posició (`position`) i el símbol que conté (`symbol`). El símbol pot ser "X", "O" o `null` si la casella està buida.

**BoardModel.kt**

Conté les dades principals del joc i tota la lògica de negoci.

Les seves propietats són:

-   `cells`: llista de les nou caselles.
-   `currentPlayer`: jugador que té el torn actual.
-   `winner`: jugador que ha guanyat.
-   `isDraw`: indica si hi ha empat.
-   `isGameOver`: indica si la partida ha acabat.

També conté tres funcions importants:

-   `createInitialBoard()`: crea un tauler nou amb nou caselles buides.
-   `makeMove(position)`: realitza una jugada i comprova que sigui vàlida.
-   `checkWinner()`: comprova les combinacions guanyadores i detecta si hi ha empat.

Per actualitzar les dades utilitza `.copy()`, creant nous objectes sense modificar directament els originals.

## 2.2. ViewModel

Dins de la carpeta `viewmodel` hi ha `GameViewModel.kt`.

S'encarrega de gestionar l'estat del joc i comunicar-se amb el Model.

Utilitza `MutableStateFlow` per guardar l'estat modificable del tauler i `asStateFlow()` per exposar-lo de manera que la interfície només el pugui consultar.

Les seves funcions principals són:

-   `onCellClick(position)`: rep la posició seleccionada i demana al `BoardModel` que realitzi la jugada.
-   `restartGame()`: crea un tauler nou i reinicia la partida.

## 2.3. View (Interfície gràfica)

Dins de la carpeta `view` hi ha tres fitxers:

**GameScreen.kt**

És la pantalla principal del joc. Utilitza `collectAsState()` per observar els canvis de l'estat del tauler.

Mostra el títol, el torn del jugador, el resultat de la partida, el tauler i el botó per reiniciar.

**BoardGrid.kt**

Construeix el tauler de 3x3 utilitzant `LazyVerticalGrid`, que organitza les nou caselles en tres columnes.

Rep la llista de caselles i comunica la posició seleccionada a `GameScreen`.

**CellItemView.kt**

Representa visualment cada casella mitjançant un component `Card` de Material Design.

Mostra una X de color blau, una O de color vermell o deixa la casella buida. També impedeix seleccionar una casella que ja està ocupada.

## 2.4. MainActivity.kt

És el punt d'entrada de l'aplicació Android.

Utilitza `setContent()` per iniciar la interfície amb Jetpack Compose i mostrar `GameScreen()`.

## 2.5. Funcionament de l'arquitectura

El funcionament del joc segueix aquests passos:

1.  L'usuari selecciona una casella del tauler.
2.  `BoardGrid` comunica la posició seleccionada a `GameScreen`.
3.  `GameScreen` crida la funció `onCellClick()` del `GameViewModel`.
4.  El ViewModel demana al `BoardModel` que realitzi la jugada.
5.  El nou estat del tauler es guarda a `MutableStateFlow`.
6.  `collectAsState()` detecta el canvi i Jetpack Compose actualitza la interfície.


# 3\. Conceptes i Mètodes Apresos

Durant el desenvolupament del projecte he après a utilitzar diferents funcions i eines de Kotlin, Android i Jetpack Compose.

## 3.1. MutableStateFlow i asStateFlow()

`MutableStateFlow` permet guardar i modificar un estat. `asStateFlow()` permet exposar aquest estat perquè altres components només el puguin consultar.

Exemple:

kotlin

private val \_contador = MutableStateFlow(0)  
val contador = \_contador.asStateFlow()  
  
fun incrementar() {  
    \_contador.value++  
}  

## 3.2. collectAsState()

Permet observar els canvis d'un Flow des de Jetpack Compose. Quan el valor canvia, la interfície s'actualitza automàticament.

Exemple:

kotlin

val contador by contadorFlow.collectAsState()  
  
Text(text = "Valor: $contador")  

## 3.3. copy()

Permet crear una còpia d'un objecte d'una `data class` canviant només les propietats necessàries, sense modificar l'objecte original.

Exemple:

kotlin

data class Jugador(  
    val nom: String,  
    val punts: Int  
)  
  
val jugador = Jugador("X", 0)  
val jugadorNou = jugador.copy(punts = 1)  

## 3.4. LazyVerticalGrid

Permet mostrar elements en forma de quadrícula vertical. En aquest projecte s'utilitza per construir el tauler de tres columnes.

Exemple:

kotlin

LazyVerticalGrid(  
    columns = GridCells.Fixed(3)  
) {  
    items(caselles) { casella ->  
        Text(text = casella.symbol ?: "")  
    }  
}  

## 3.5. @Composable

És una anotació que permet crear funcions que construeixen elements visuals amb Jetpack Compose.

Exemple:

kotlin

@Composable  
fun Missatge() {  
    Text("Hola!")  
}  

## 3.6. Modifier

Permet modificar les característiques visuals dels components, com ara la mida, els marges o les interaccions.

Exemple:

kotlin

Text(  
    text = "Hola",  
    modifier = Modifier.padding(16.dp)  
)  

## 3.7. viewModel()

Permet obtenir una instància del ViewModel des d'una funció Composable.

Exemple:

kotlin

val gameViewModel: GameViewModel = viewModel()  

## 3.8. map()

Permet transformar els elements d'una llista i crear-ne una de nova.

Exemple:

kotlin

val numeros = listOf(1, 2, 3)  
val dobles = numeros.map { it \* 2 }  

Resultat: `[2, 4, 6]`.

## 3.9. all()

Permet comprovar si tots els elements d'una llista compleixen una condició.

Exemple:

kotlin

val numeros = listOf(2, 4, 6)  
val totsParells = numeros.all { it % 2 == 0 }  

Resultat: `true`.

## 3.10. setContent()

Permet iniciar la interfície de Jetpack Compose des d'una activitat Android.

Exemple:

kotlin

setContent {  
    GameScreen()  
}  

## 3.11. Operador Elvis (?:)

Permet establir un valor alternatiu quan una variable és `null`.

Exemple:

kotlin

val simbol: String? = null  
val resultat = simbol ?: ""  

## 3.12. dp i sp

Són unitats de mesura d'Android:

-   `dp`: s'utilitza per definir dimensions i espais.
-   `sp`: s'utilitza principalment per definir la mida del text.

## 3.13. data class

És una classe de Kotlin pensada per guardar dades. Genera automàticament funcions com `copy()`, `equals()` i `hashCode()`.