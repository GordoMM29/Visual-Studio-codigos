#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#define SIZE 5 //capacidad del arreglo que se utilizara 

int main(){
//2 pipes, uno envia y otro recibe
int fd1[2], fd2[2]; //file descriptor
pid_t pid;

 
int numeros[SIZE] = {15, 8, 42, 9, 28}; 
int suma= 0;

//pips
if(pipe(fd1)== -1 || pipe(fd2)== -1){
    perror ("No se crearon los pipes");
    exit(1);

}
pid= fork();
if (pid<0){
    perror("Un fallo al crear el proceso");
    exit(1);

}
else if (pid==0){//hijo
close(fd1[1]);//Cierra escritura p1
close(fd2[0]);//Cierra lectura p2
int numeros_hijo[SIZE];
//Leer datos del padre
read(fd1[0], numeros_hijo, sizeof(numeros_hijo));
close(fd1[0]);

//Calcular la suma de los datos enviados por el padre
for (int i=0; i<SIZE; i++){
    suma += numeros_hijo[i];
}

//Enviar la suma al padre
write(fd2[1], &suma, sizeof(suma));
close(fd2[1]);
exit(0);
}

else {//padre
close(fd1[0]);//Cierra lectura del p1
close(fd2[1]);//Cierra escritura del p2
//Enviar datos al hijo
write(fd1[1], numeros, sizeof(numeros));
close(fd1[1]);
//Recibir la suma calculada del Hijo - Hijo 
//escribir la suma calculada del Hijo
read(fd2[0], &suma, sizeof(suma));
close(fd2[0]);
printf("La suma que se realizo es: %d\n", suma );
}

return 0;
    }
