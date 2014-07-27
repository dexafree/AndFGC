# AndFGC (Català)
AndFGC és una aplicació de codi lliure creada per facilitar la interacció dels usuaris amb els serveis de Ferrocarrils de la Generalitat de Catalunya.

Aquesta aplicació neix a partir de la necessitat d'una aplicació que complís, entre d'altres, amb les guies de disseny per a Android proporcionades per Google.

A més, després de veure el codi font de la original, vaig creure que era necessari fer alguns canvis en la forma d'obtenir les dades mostrades, i vaig considerar que tenia la capacitat de millorar l'aplicació actual i adaptar-la a les meves necessitats.

Com considero que la informació hauria de ser lliure, ofereixo a qualsevol que cregui que pot millorar-la o afegir funcionalitats que agafi aquest desenvolupament com a base i que modifiqui el que cregui adient.

## L'aplicació
AndFGC ofereix la possibilitat de:
- Fer cerques de trajectes en funció de sortida, arribada i horaris
- Guardar aquestes cerques com a favorites per tenir-les a mà en futures ocasions
- Veure horaris alternatius als triats per al mateix trajecte
- Consultar l'estat del servei i veure les alertes disponibles
- Descarregar i visualitzar els horaris de les diferents linies
- Veure les tarifes actualitzades i obtenir-ne informació
- Veure totes les parades de FGC a un mapa, i fins i tot, iniciar una cerca de trajecte des del mapa
- Llegir els últims twits de la compta oficial de FGC a Twitter

AndFGC compta amb un estil cuidat que segueix els patrons de disseny suggerits per Google per tal de tenir una aparença semblant a totes les aplicacions, proporcionant així una experiència d'usuari intuitiva i satisfactòria.
A més, és compatible amb versions d'Android a partir de 2.3, i el seu estil i funcionalitats s'adapten a la versió d'Android que estigui executant el telèfon.

## Captures de pantalla
Veure al final

## Base de dades
Durant el desenvolupament de l'aplicació no vaig trobar cap base de dades que tingués les relacions de linia-parades, parades-abreviatures, parades-coordenades... Així que vaig fer la tasca de recerca, recopilar les dades i crear la base de dades.
Per si algún dia una persona volgués començar un projecte relacionat amb FGC, que no dubti en agafar la base de dades, la qual està alliberada sota llicencia Apache v2 i es troba a la ruta app/src/main/assets/parades.sqlite.

# AndFGC (Castellano)
AndFGC es una aplicación de código libre creada para faficlitar la interacción de los usuarios con los servicios de Ferocarrils de la Generalitat de Catalunya.

Esta aplicación nace a partir de la necesidad de una aplicación que cumpliera, entre otras cosas, con las guías de diseño para Android proporcionadas por Google.

Además, después de ver el código fuente de la original, creí que era necesario hacer algunos cambios en la forma de obtener los datos mostrados, y consideré que tenía la capacidad de mejorar la aplicación actual y adaptarla a mis necesidades.

Como considero que la información debería ser libre, ofrezco a cualquiera que crea que puede mejorarla o añadir funcionalidades que coja este desarrollo como base y que modifique lo que crea conveniente.

## La aplicación
AndFGC ofrece la posibilidad de:

- Hacer búsquedas de trayectos en función de salida, llegada, y horarios
- Guardar estas búsquedas como favoritas para tenerlas a mano en futuras ocasiones
- Ver horarios alternativos a los escogidos para el mismo trayecto
- Consultar el estado del servicio y ver las alertas disponibles
- Descargar y visualizar los horarios de las diferentes líneas
- Ver las tarifas actualizadas y obtener información acerca de ellas
- Ver todas las paradas de FGC en un mapa, e incluso, iniciar una búsqueda de trayecto desde el mapa
- Leer los últimos twits de la cuenta oficial de FGC en Twitter

AndFGC cuenta con un estilo cuidado que sigue los patrones de diseño sugeridos por Google por tal de tener una apariencia similar en todas las aplicaciones, proporcionando así una experiencia de usuario intuitiva y satisfactoria.
Además, es compatible con versiones de Android a partir de 2.3, y su estilo yb funcionalidades se adaptan a la versión de Android que esté ejecutando el teléfono.

## Capturas de pantalla
Ver al final

## Base de datos
Durante el desarrollo de la aplicación no encontré ninguna base de datos que tuviera las relaciones de línea-paradas, paradas-abreviaturas. paradas-coordenadas.. Así que realicé una tarea de investigación y recopilación de datos, y cree la base de datos.
Por si algún día una persona quisiera empezar un proyecto relacionado con FGC, que no dude en coger esa base de datos, la cual está liberada bajo licencia Apache v2 y se encuentra en la ruta app/src/main/assets/parades.sqlite.


# Screenshots
![Imgur](http://i.imgur.com/BgRqhO0.png)
![Imgur](http://i.imgur.com/s4C9U2v.png)
![Imgur](http://i.imgur.com/fGk3C9E.png)
![Imgur](http://i.imgur.com/SJ5nMBs.png)
![Imgur](http://i.imgur.com/ikdHmG5.png)
![Imgur](http://i.imgur.com/iHtI5PG.png)
![Imgur](http://i.imgur.com/NHXkMF7.png)
![Imgur](http://i.imgur.com/HsEWzp0.png)
![Imgur](http://i.imgur.com/MSQDjcd.png)
![Imgur](http://i.imgur.com/pMLYNx8.png)

# Artículos relacionados

- [Sorteando la autenticación para operaciones básicas en Twitter](http://www.dexa-dev.com/sorteando-la-autenticacion-para-operaciones-basicas-en-twitter/)
- [Bypassing authentication for basic Twitter operations](http://www.dexa-dev.com/bypassing-authentication-for-basic-twitter-operations/)
- [Extrayendo coordenadas de las paradas de FGC](http://www.dexa-dev.com/python-extrayendo-coordenadas-de-las-paradas-de-fgc/)

# Download
https://github.com/dexafree/AndFGC/releases/

# LICENSE

This app and its database are licensed under Apache v2:

>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
>You may obtain a copy of the License at

>    http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
>See the License for the specific language governing permissions and limitations under the License.

This means you can use the application in whatever way you want, and also I take no responsibility of that uses.
