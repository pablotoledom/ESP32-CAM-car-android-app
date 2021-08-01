
# ESP32-CAM-car-android-app

Aplicación Android para vehículo a control remoto mediante WIFI.

Esta aplicación es parte del proyecto: [ESP32-CAM-car-server](https://github.com/pablotoledom/ESP32-CAM-car-server), donde podrá montar un vehículo a control remoto usando un micro-controlador ESP32-CAM.

Si ya construyó el vehículo y desea manejarlo con esta aplicación, lea completamente este archivo ya que puede serle de gran ayuda.

[Click here for english readme](https://github.com/pablotoledom/ESP32-CAM-car-android-app/blob/main/README.md)

## Comenzando 🚀

Si no conoce el vehículo a control remoto WIFI, acá un video del resultado:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/02oBJucxMBU/0.jpg)](https://www.youtube.com/watch?v=02oBJucxMBU)

Algunas capturas de esta aplicación:

![alt Screenshot port](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/capture_port.png)

![alt Screenshot land](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/capture_land.png)

### Pre-requisitos 📋

- Android Estudio
- Dispositivo físico o virtual con mínimo Android Jelly Bean 4.1 (API 16), recomendado Android Oreo 8.0 (API 26)

### Instalación del software 🔧

#### 1) clonar el proyecto desde Github

Ejecuta el siguiente comando en tu consola

```console
git clone https://github.com/pablotoledom/ESP32-CAM-car-android-app.git
```

#### 2) Abrir el proyecto con el IDE de Android Studio

Luego de abrir el proyecto verificar que las dependencias se hayan descargado correctamente, en caso contrario puede volver a correr su archivo Gradle.


## Despliegue 📦

Método 1: Puede descargar el archivo APK e instalar directamente en su dispositivo, descárguelo desde la sección "release".

Método 2: Compilar el ejecutable usando el IDE de Android Studio, este método puede resultar más útil para quienes posean conocimientos en programación de aplicaciones Android o en el lenguaje Java, también podrá realizar cambios o mejoras a esta aplicación.


## Conectarse al Vehículo 🎮

Cuando enciende el vehículo y este no logra conectarse a una red wifi dentro de un plazo de 10 segundos levantará automáticamente un punto de acceso WIFI llamado "Remote WIFI Car".

![alt Connectar WIFI](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/capture_wifi.png)

Luego que se conecte a dicha red, deberá abrir la aplicación en su dispositivo.

Si luego de 10 segundos no visualiza este punto de acceso WIFI es posible que el vehículo ya se encuentre enlazado a una red WIFI.

### La aplicación:

Al abrir la aplicación lo primero que verá es una guía de usuario en español o inglés, el lenguaje cambiará dependiendo del idioma configurado en su dispositivo.


![alt Guia de usuario](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/screenshot_guide.png)

Esta guía entrega una ayuda rápida para comenzar a utilizar la aplicación. Presione el botón al final de la guía para pasar a la pantalla de control del vehículo.

Lo primero que hará la aplicación es buscar el vehículo en la red, ya sea que esté conectado al WIFI de su hogar o al WIFI del vehículo la aplicación recorrerá el rango de IPs hasta encontrar la correcta.

![alt Buscando vehículo](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/screenshot_search.png)

Cuando la aplicación reconoce la IP del vehículo se habilita para poder controlarlo, la vista de control es la siguiente:

![alt Pantalla de control](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/screenshot.png)

Esta pantalla permite mover el vehículo, encender la luz, tocar la bocina y cambiar algunas configuraciones.

#### Sección de configuración:

En la sección de configuración podrá escanear las redes WIFI cercanas, una vez que visualice la suya, seleccione la e ingrese la contraseña si corresponde. Luego de guardar los cambios es necesario **reiniciar el vehículo y esta aplicación**.

 ![alt Configurar WIFI](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/screenshot_wifi.png)

#### Interfaz de usuario y control del vehículo:

También podrá establecer la interfaz de usuario que más le acomode, puede elegir entre botones o joystick virtual.

![alt Configurar UI](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/screenshot_ui.png)

Si posee un **gamepad o teclado** conectado al dispositivo, este se configurará en forma automática, simplemente maneje el vehículo por medio de los botones o ejes análogos.

![alt Interfaz humana](https://raw.githubusercontent.com/pablotoledom/ESP32-CAM-car-android-app/main/images/human-controls.png)

Botones configurados, puede usar cualquiera de los siguientes:

Gamepad:
   **Avanzar**: Análogo Arriba - Botón Arriba - R1 - R2
   **Retroceder**: Análogo Abajo - Botón Abajo - L1 - L2
   **Girar Izquierda**: Análogo Izquierda - Botón Izquierda
   **Girar Derecha**: Análogo Derecha - Botón Derecha
   **Encender / Apagar Luz**: Botón X
   **Tocar Bocina**: Botón A
   **Ingresar a Configuración**: Botón Start

Teclado:
**Avanzar**: Tecla Arriba
   **Retroceder**: Tecla Abajo
   **Girar Izquierda**: Tecla Izquierda
   **Girar Derecha**: Tecla Derecha
   **Encender / Apagar Luz**: Tecla Q
   **Tocar Bocina**: Tecla Espacio
   **Ingresar a Configuración**: Tecla W

## Autor

Pablo Toledo


## Licencia 📄

Este proyecto está bajo la Licencia Apache, Versión 2.0 - mira el archivo [LICENCIA](https://github.com/pablotoledom/ESP32-CAM-car-android-app/blob/main/LICENCIA) para detalles.