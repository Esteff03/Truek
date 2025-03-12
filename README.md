**TRUEK** 📱🚀🚀 

Aplicación móvil para intercambio de productos o servicios, sin la necesidad de utilizar dinero, ya que se pueden realizar trueques. 

![Screen_recording_20250129_213939-ezgif com-resize](https://github.com/user-attachments/assets/bbfdb1c2-75d3-4a9b-a46f-8f4c6e6102f2)





[Trello](https://trello.com/invite/b/674da37a74943d6ec98d884d/ATTI4506a54cb7a89d297eabd50201a6eaccCEE87838/truek)

[Figma](https://www.figma.com/design/g5gElOIdZWXgq2P8IEJk5P/Truek?node-id=0-1&p=f&t=Wh73LkqWBYenrGI8-0)


*Splash*


![image](https://github.com/user-attachments/assets/294b8039-f587-44d9-bef3-6896ec8cdf32)




![image](https://github.com/user-attachments/assets/5b278731-dc9d-45d4-8e8a-dc8a3826aed9)






El objetivo es fomentar una economía colaborativa donde las personas puedan satisfacer sus necesidades, reducir el
desperdicio y reutilizar recursos, todo a través del intercambio justo.



*Login*

<div style="display: flex; justify-content: center;">
  <img src="https://github.com/user-attachments/assets/c090b980-b671-406e-846b-9e584a9de668" width="200" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/d61ec46a-9676-4242-b03f-2d88264e5463" width="200" />
</div>











[Figma](https://www.figma.com/design/g5gElOIdZWXgq2P8IEJk5P/Truek?node-id=0-1&p=f&t=Wh73LkqWBYenrGI8-0)





[Últimas actualizaciones 30-01-2025]


*Splash*


<div style="display: flex; justify-content: center;">
  <img src="https://github.com/user-attachments/assets/fe36a831-5744-4783-b0a8-a6b3f70678cb" width="200" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/6f0e9065-7cea-44ec-a313-1db72baa34e9" width="200" />
</div>






*Profile*


Gif-profile:



![Profile-ezgif com-crop (2)](https://github.com/user-attachments/assets/7118111f-1481-4e15-b72e-ba17491542a0)


**Dentro del profile hay 2 cardViews que se utilizaran para mostrar el monedero virtual y el numero de intercambios que ha realizado el usuario. 
De momento, el posible código detras del método que realize la lógica de actualizar monedero según los intercambios que se realizen es el siguiente:**


Button modifyComprar = findViewById(R.id.btnComprar);  // Botón de compra
        modifyComprar.setOnClickListener(v -> {
            int contador = Integer.parseInt(exchange.getText().toString());
            int valor = 7; // Valor del producto que queremos comprar
            final int AUMENTO_MONEDERO = 15; // Cada vez que haga un intercambio, aumenta 15 euros

            // Actualiza el valor del monedero después de la compra
            int saldoActual = Integer.parseInt(payment.getText().toString());
            payment.setText(String.valueOf(saldoActual - valor)); // Resta el valor del producto del monedero
            contador++;

            if (contador > 0) {
                // Actualiza los intercambios realizados y aumenta el monedero
                exchange.setText(String.valueOf(contador));
                payment.setText(String.valueOf(Integer.parseInt(payment.getText().toString()) + AUMENTO_MONEDERO));
            } else {
                // Si no hay intercambios, muestra 0
                exchange.setText("0");
            }

            // Muestra un Toast
            Toast.makeText(Profile.this, "Compra realizada", Toast.LENGTH_SHORT).show();
        });


*Main* +  *Menú AppBar*

<div style="display: flex; justify-content: center;">
  <img src="https://github.com/user-attachments/assets/ef54629b-3702-422f-9c41-6bbe28247c40" width="200" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/c06befa0-417e-43ad-94c9-436fac257571" width="200" />
</div>





Aplicaciçon finalizada: 

![image](https://github.com/user-attachments/assets/4a1b0249-14f4-4ec2-80b4-9d60eca7f263)

![image](https://github.com/user-attachments/assets/1aed303e-4b29-43b2-b9d3-0e490e5014e6)

![image](https://github.com/user-attachments/assets/94e7c410-51b1-4566-b45c-bb6435a33184)


![image](https://github.com/user-attachments/assets/85f44915-c29c-4da5-9c9f-fdc840877ba8)

![image](https://github.com/user-attachments/assets/cf17aff8-38ee-4c8b-b1c6-8ac8cc0218cb)

![image](https://github.com/user-attachments/assets/ed978a8a-d698-4db9-a19f-f9da3ca5d8d4)


Roles de desarrollo:  

*Scrum Master: Benjamin*

*Cloud Master: Sindy*

*Designer: Javier*

*Designer: Sergio*



*Proyecto educativo realizado por alumnos del IES Juan de la Cierva.*



