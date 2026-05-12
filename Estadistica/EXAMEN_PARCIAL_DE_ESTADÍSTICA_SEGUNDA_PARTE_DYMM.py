# %% [markdown]
# <font size="5">
#  
#  
# ---
# ---
#  
# $$ $$
#  
# $$ \large{
# \begin{array} {rcr}
# &\mbox{UNIVERSIDAD AUTÓNOMA DE COAHUILA}&  \\
# &\mbox{FACULTAD DE SISTEMAS}& \\
# &\mbox{ESTADÍSTICA}   & \\
# &\mbox{Parcial 1. segunda parte}& \\
# &\mbox{Jesus Cedillo Reyna} \\
# \end{array}
# } 
# $$
#  
# $$ $$
# En el archivo _**Educacion_modificada.xlsx**_ se muestra el grado promedio de escolaridad de la población de 15 años o más, dado por entidad federativa según sexo. Los años en los que se realizaron estos estudios fueron del 2000 a 2020 y se tomó como referencia un muestreo aleatorio simple para la obtención de los datos en cada entidad federativa y el tamaño de muestra en cada uno de los estados fue de $1000$ personas.  

# %%


# %% [markdown]
# <font size="5"> 
# $\bullet $ Definición:
#  
# El Número de años que, en promedio, aprobaron las personas de 15 años o más, en el Sistema Educativo Nacional. Resulta de dividir la suma de los años aprobados desde el primero de primaria hasta el último grado alcanzado de las personas de 15 años o más entre el total de la población de 15 años o más.
#  
#  
# $$ $$
#  
# ---
# ---

# %% [markdown]
# ![Captura de pantalla 2025-04-10 a la(s) 5.47.36 p.m..png](attachment:2ac59337-08cc-4895-8a3a-3f194c94388c.png)

# %% [markdown]
# <font size="4">
#  
# ---
# ---
#  
# <span style="color:red">     
#  
# **_TLC_ :**  
#  
# _Si $X_1,X_2,\ldots,X_n$ es una secuencias de $n$ variables independientes con $\ \ E(X_i)=\mu_i$, $ \ \ \ V(X_i)=\sigma_i^2 \hspace{1cm} $ con $\hspace{1cm}Y=X_1+X_2+\cdots+X_n \hspace{1cm}$ Entonces bajo ciertas condiciones generales se tiene:_
# $$ $$
# $$ \displaystyle Y \sim \cal{Normal}\left(\sum_{i=1}^n \mu_i,\displaystyle \sqrt{ \sum_{i=1}^n \sigma_i^2 }\right) $$
# $$ $$
# 
# $$Z_n=\frac{Y- \displaystyle \sum_{i=1}^n \mu_i}{ \sqrt{ \displaystyle \sum_{i=1}^n \sigma_i^2}}$$
# $$ Z_n \sim \cal{Normal}\left(0,1\right)$$
#  
#  
# ---
# ---

# %% [markdown]
# <font size="4">
#  
# **Resultado directo**
# _Si $X_1,X_2,\ldots,X_n$ es una secuencias de $n$ variables INDEPENDIENTES e IDENTICAMENTE DISTRIBUIDAS *(i.i.d.)*,_ 
# con $\ \ E(X_i)=\mu$, $ \ \ \ V(X_i)=\sigma^2 \ \ $  Entonces  
#  
# $$ $$
#  
# $\ \ \ Y=X_1+X_2+\cdots+X_n \hspace{1cm}$ _Entonces bajo ciertas condiciones generales se tiene:_
# 
# $$
# \begin{array}{rcl} 
# Z & = & \displaystyle \frac{x-\mu}{ \sigma }\sim N(0,1) \\
# \end{array}
# $$
#  
#     
# \begin{array}{rcl}  
# &&\\ 
# Z_n=\displaystyle \frac{Y- \displaystyle \sum_{i=1}^n \mu_i}{ \sqrt{ \displaystyle \sum_{i=1}^n \sigma_i^2}}
# & == \Longrightarrow & \displaystyle \frac{Y- \displaystyle n \mu}{  \sqrt{ \displaystyle n \sigma^2}}\\
# & & \\
# Z_n &=& \displaystyle \frac{\left(\frac{1}{n}\right)(Y- \displaystyle n \mu)}{\left(\frac{1}{n}\right)
#     \left(\sqrt{ \displaystyle n \sigma^2 }\right)}\\
# & &\\ 
# Z_n &=& \displaystyle \frac{\left(\frac{1}{n}\right)(Y- \displaystyle n \mu)}{\frac{
#     \left(\sqrt{ \displaystyle n \sigma^2 }\right)}{n}}\\
# & & \\
# Z_n & = &\displaystyle \frac{\bar{Y}- \displaystyle \mu}{\displaystyle\sqrt{\frac{\sigma^2}{n}}}\\
# && \\
# \end{array}    
# 
#  
# <font size="5">
# 
# <span style="color:red"> 
#     $$Y=X_1+X_2+\cdots+X_n \hspace{.5cm} = \Longrightarrow
# \hspace{.5cm} Z_n =\displaystyle \frac{\bar{Y}- \displaystyle \mu}{\displaystyle \frac{\sigma}{\sqrt{n}}}$$
# </span>
# 
#  
# $$ $$
#  
#  
# <font size="5">
#  
# Tiene una distribución normal $ \cal{N(0,1)}  $
#  
#     
# $$ $$
#  
# <font size="4">    
# Link relacionado a [TLC](https://support.minitab.com/es-mx/minitab/18/help-and-how-to/statistics/basic-statistics/supporting-topics/data-concepts/about-the-central-limit-theorem/)     
#  
#  
# ---
# ---

# %%
import pandas as pd

fila_inicio_2000 = 15
fila_fin_2000 = 47
fila_inicio_2010 = 47
fila_fin_2010 = 79
fila_inicio_2015 = 79
fila_fin_2015 = 111
fila_inicio_2020 = 111
fila_fin_2020 = 143

filas_a_omitir_2000 = fila_inicio_2000 - 1
filas_a_omitir_2010 = fila_inicio_2010 - 1
filas_a_omitir_2015 = fila_inicio_2015 - 1
filas_a_omitir_2020 = fila_inicio_2020 - 1

df_2000 = pd.read_excel('Educacion_modificada.xlsx', skiprows=filas_a_omitir_2000, nrows=fila_fin_2000 - filas_a_omitir_2000 - 1)
df_2010 = pd.read_excel('Educacion_modificada.xlsx', skiprows=filas_a_omitir_2010, nrows=fila_fin_2010 - filas_a_omitir_2010 - 1)
df_2015 = pd.read_excel('Educacion_modificada.xlsx', skiprows=filas_a_omitir_2015, nrows=fila_fin_2015 - filas_a_omitir_2015 - 1)
df_2020 = pd.read_excel('Educacion_modificada.xlsx', skiprows=filas_a_omitir_2020, nrows=fila_fin_2020 - filas_a_omitir_2020 - 1)

df_2010.columns = df_2000.columns
df_2015.columns = df_2000.columns
df_2020.columns = df_2000.columns



# %%
print("Ano 2000")
df_2000

# %%
print("Ano 2010")
df_2010

# %%
print("Ano 2015")
df_2015

# %%
print("Ano 2020")
df_2020

# %% [markdown]
# ---
# ---
#  
# <font size="4"> 
# <span style="color:blue">
#  
# 1) Usando como referencia los de datos  del archivo _**Educacion_modificada.xlsx**_ 
# 
#  
# <span style="color:red">
#  
#  
# a) Realice una representación gráfica que involucre los histogramas asociados a Hombres-2000, Hombres-2010, Hombres-2015, Hombres-2020 (en una sola ventana de graficación.)   
# b) Realice una representación gráfica que involucre los histogramas asociados a Mujeres-2000, Mujeres-2010, Mujeres-2015, Mujeres-2020 (en una sola ventana de graficación.)   

# %% [markdown]
# <h1>:</h1>

# %%

df_2000.describe()


# %%
df_2010.describe()

# %%
df_2015.describe()

# %%


df_2020.describe()

# %% [markdown]
# <h1>a)</h1>

# %%

import matplotlib.pyplot as plt

plt.figure(figsize=(10, 8))

plt.subplot(2, 2, 1)
plt.hist(df_2000['Hombres'], bins=30, color='gray', edgecolor='black')
plt.title('Hombres-2000')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(2, 2, 2)
plt.hist(df_2010['Hombres'], bins=30, color='green', edgecolor='black')
plt.title('Hombres-2010')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(2, 2, 3)
plt.hist(df_2015['Hombres'], bins=30, color='orange', edgecolor='black')
plt.title('Hombres-2015')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(2, 2, 4)
plt.hist(df_2020['Hombres'], bins=30, color='aqua', edgecolor='black')
plt.title('Hombres-2020')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.suptitle('Histogramas de Hombres (2000, 2010, 2015, 2020)')
plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()



# %% [markdown]
# <h1>b)</h1>

# %%

import matplotlib.pyplot as plt

plt.figure(figsize=(10, 8))

plt.subplot(2, 2, 1)
plt.hist(df_2000['Mujeres'], bins=30, color='gray', edgecolor='black')
plt.title('Mujeres-2000')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(2, 2, 2)
plt.hist(df_2010['Mujeres'], bins=30, color='green', edgecolor='black')
plt.title('Mujeres-2010')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(2, 2, 3)
plt.hist(df_2015['Mujeres'], bins=30, color='orange', edgecolor='black')
plt.title('Mujeres-2015')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(2, 2, 4)
plt.hist(df_2020['Mujeres'], bins=30, color='aqua', edgecolor='black')
plt.title('Mujeres-2020')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.suptitle('Histogramas de Mujeres (2000, 2010, 2015, 2020)')
plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()

# %%

df_combined = pd.concat([df_2000, df_2010, df_2015, df_2020])
df_combined.describe()

# %% [markdown]
# ---
# ---
#  
# <font size="4"> 
# <span style="color:blue">
#  
# 2) Usando como referencia los de datos  del archivo _**Educacion_modificada.xlsx**_ 
# 
#  
# <span style="color:red">
#  
#  
# a) Realice una representación gráfica que involucre los gráficos de cajas (BOXPLOT) asociados a _Hombres-2000_, _Hombres-2010_, _Hombres-2015_, _Hombres-2020_ (en una sola ventana de graficación.)   
# b) Realice una representación gráfica que involucre los gráficos de cajas (BOXPLOT) asociados a _Mujeres-2000_, _Mujeres-2010_, _Mujeres-2015_, _Mujeres-2020_ (en una sola ventana de graficación.)   
#  
# c) Realice una representación gráfica que involucre los gráficos de cajas (BOXPLOT) usando el siguiente orden: _Hombres-2000_, _Mujeres-2000_, _Hombres-2010_, _Mujeres-2010_,  _Hombres-2015_, _Mujeres-2015_, _Hombres-2020_, _Mujeres-2020_ (en una sola ventana de graficación.)   
#  

# %% [markdown]
# <h1>a)</h1>

# %%

import matplotlib.pyplot as plt

plt.figure(figsize=(12, 8))

plt.subplot(2, 2, 1)
plt.boxplot(df_2000['Hombres'], patch_artist=True, boxprops=dict(facecolor='gray'))
plt.title('Hombres-2000')
plt.ylabel('Grado promedio de escolaridad')

plt.subplot(2, 2, 2)
plt.boxplot(df_2010['Hombres'], patch_artist=True, boxprops=dict(facecolor='green'))
plt.title('Hombres-2010')
plt.ylabel('Grado promedio de escolaridad')

plt.subplot(2, 2, 3)
plt.boxplot(df_2015['Hombres'], patch_artist=True, boxprops=dict(facecolor='orange'))
plt.title('Hombres-2015')
plt.ylabel('Grado promedio de escolaridad')

plt.subplot(2, 2, 4)
plt.boxplot(df_2020['Hombres'], patch_artist=True, boxprops=dict(facecolor='aqua'))
plt.title('Hombres-2020')
plt.ylabel('Grado promedio de escolaridad')

plt.suptitle('Gráficos de cajas (BOXPLOT) de Hombres (2000, 2010, 2015, 2020)')
plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()


# %% [markdown]
# <h1>b)</h1>

# %%

plt.figure(figsize=(12, 8))

plt.subplot(2, 2, 1)
plt.boxplot(df_2000['Mujeres'], patch_artist=True, boxprops=dict(facecolor='gray'))
plt.title('Mujeres-2000')
plt.ylabel('Grado promedio de escolaridad')

plt.subplot(2, 2, 2)
plt.boxplot(df_2010['Mujeres'], patch_artist=True, boxprops=dict(facecolor='green'))
plt.title('Mujeres-2010')
plt.ylabel('Grado promedio de escolaridad')

plt.subplot(2, 2, 3)
plt.boxplot(df_2015['Mujeres'], patch_artist=True, boxprops=dict(facecolor='orange'))
plt.title('Mujeres-2015')
plt.ylabel('Grado promedio de escolaridad')

plt.subplot(2, 2, 4)
plt.boxplot(df_2020['Mujeres'], patch_artist=True, boxprops=dict(facecolor='aqua'))
plt.title('Mujeres-2020')
plt.ylabel('Grado promedio de escolaridad')

plt.suptitle('Gráficos de cajas (BOXPLOT) de Mujeres (2000, 2010, 2015, 2020)')
plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()


# %% [markdown]
# <h1>C)</h1>

# %%

data_to_plot = [df_2000['Hombres'], df_2000['Mujeres'], df_2010['Hombres'], df_2010['Mujeres'], df_2015['Hombres'], df_2015['Mujeres'], df_2020['Hombres'], df_2020['Mujeres']]
colors = ['gray', 'gray', 'green', 'green', 'orange', 'orange', 'aqua', 'aqua']
labels = ['Hombres-2000', 'Mujeres-2000', 'Hombres-2010', 'Mujeres-2010', 'Hombres-2015', 'Mujeres-2015', 'Hombres-2020', 'Mujeres-2020']

fig = plt.figure(figsize=(12, 8))
boxplots = plt.boxplot(data_to_plot, patch_artist=True)

for patch, color in zip(boxplots['boxes'], colors):
    patch.set_facecolor(color)

plt.xticks(range(1, len(labels) + 1), labels)
plt.title('Gráficos de cajas (BOXPLOT) combinados')
plt.ylabel('Grado promedio de escolaridad')
plt.xticks(rotation=45)
plt.tight_layout()
plt.show()


# %% [markdown]
# ---
# ---
#  
# <font size="4"> 
# <span style="color:blue">
#  
# 3) Usando como referencia los datos  del archivo _**Educacion_modificada.xlsx**_ 
# 
#  
# <span style="color:red">
#  
#  
# a) Realice una representación gráfica que involucre los intervalos de confianza asociados a _Hombres-2000_, _Hombres-2010_, _Hombres-2015_, _Hombres-2020_ (en una sola ventana de graficación.)   
# b) Realice una representación gráfica que involucre los intervalos de confianza  asociados a _Mujeres-2000_, _Mujeres-2010_, _Mujeres-2015_, _Mujeres-2020_ (en una sola ventana de graficación.)   

# %% [markdown]
# <h1>a)</h1>

# %%

import numpy as np
from scipy import stats
import matplotlib.pyplot as plt
from scipy.stats import norm

def intervalo_confianza(data, alpha=0.05):
    media = np.mean(data)
    sem = stats.sem(data)
    intervalo = stats.t.interval(1-alpha, len(data)-1, loc=media, scale=sem)
    return media, intervalo

media_hombres_2000, intervalo_hombres_2000 = intervalo_confianza(df_2000['Hombres'])
media_hombres_2010, intervalo_hombres_2010 = intervalo_confianza(df_2010['Hombres'])
media_hombres_2015, intervalo_hombres_2015 = intervalo_confianza(df_2015['Hombres'])
media_hombres_2020, intervalo_hombres_2020 = intervalo_confianza(df_2020['Hombres'])

print(f'\nIntervalo de confianza para Hombres-2000: {intervalo_hombres_2000}')
print(f'Intervalo de confianza para Hombres-2010: {intervalo_hombres_2010}')
print(f'Intervalo de confianza para Hombres-2015: {intervalo_hombres_2015}')
print(f'Intervalo de confianza para Hombres-2020: {intervalo_hombres_2020}')

fig, ax = plt.subplots(figsize=(12, 8))

categorias = ['Hombres-2000', 'Hombres-2010', 'Hombres-2015', 'Hombres-2020']
medias = [media_hombres_2000, media_hombres_2010, media_hombres_2015, media_hombres_2020]
errores = [(intervalo_hombres_2000[1]-intervalo_hombres_2000[0])/2,
           (intervalo_hombres_2010[1]-intervalo_hombres_2010[0])/2,
           (intervalo_hombres_2015[1]-intervalo_hombres_2015[0])/2,
           (intervalo_hombres_2020[1]-intervalo_hombres_2020[0])/2]

colores = ['gray', 'green', 'orange', 'aqua']

for i, categoria in enumerate(categorias):
    media = medias[i]
    error = errores[i]
    desv = error * np.sqrt(len(df_2000))  # Desviación estándar aproximada
    x = np.linspace(media - 3*desv, media + 3*desv, 100)
    y = norm.pdf(x, media, desv)
    ax.plot(x, y, label=categoria, color=colores[i])

ax.set_xticks(medias)
ax.set_xticklabels(categorias, rotation=45, ha='right')
ax.set_title('Intervalos de confianza para Hombres (2000, 2010, 2015, 2020)')
ax.set_xlabel('Grado promedio de escolaridad')
ax.set_ylabel('Densidad de probabilidad')
ax.legend()
ax.grid(True)

plt.tight_layout()
plt.show()


# %% [markdown]
# <h1>b)</h1>

# %%

import numpy as np
from scipy import stats
import matplotlib.pyplot as plt
from scipy.stats import norm

def intervalo_confianza(data, alpha=0.05):
    media = np.mean(data)
    sem = stats.sem(data)
    intervalo = stats.t.interval(1-alpha, len(data)-1, loc=media, scale=sem)
    return media, intervalo

media_mujeres_2000, intervalo_mujeres_2000 = intervalo_confianza(df_2000['Mujeres'])
media_mujeres_2010, intervalo_mujeres_2010 = intervalo_confianza(df_2010['Mujeres'])
media_mujeres_2015, intervalo_mujeres_2015 = intervalo_confianza(df_2015['Mujeres'])
media_mujeres_2020, intervalo_mujeres_2020 = intervalo_confianza(df_2020['Mujeres'])

print(f'\nIntervalo de confianza para Mujeres-2000: {intervalo_mujeres_2000}')
print(f'Intervalo de confianza para Mujeres-2010: {intervalo_mujeres_2010}')
print(f'Intervalo de confianza para Mujeres-2015: {intervalo_mujeres_2015}')
print(f'Intervalo de confianza para Mujeres-2020: {intervalo_mujeres_2020}')

fig, ax = plt.subplots(figsize=(12, 8))

categorias = ['Mujeres-2000', 'Mujeres-2010', 'Mujeres-2015', 'Mujeres-2020']
medias = [media_mujeres_2000, media_mujeres_2010, media_mujeres_2015, media_mujeres_2020]
errores = [(intervalo_mujeres_2000[1]-intervalo_mujeres_2000[0])/2,
           (intervalo_mujeres_2010[1]-intervalo_mujeres_2010[0])/2,
           (intervalo_mujeres_2015[1]-intervalo_mujeres_2015[0])/2,
           (intervalo_mujeres_2020[1]-intervalo_mujeres_2020[0])/2]

colores = ['gray', 'green', 'orange', 'aqua']

for i, categoria in enumerate(categorias):
    media = medias[i]
    error = errores[i]
    desv = error * np.sqrt(len(df_2000)) 
    x = np.linspace(media - 3*desv, media + 3*desv, 100)
    y = norm.pdf(x, media, desv)
    ax.plot(x, y, label=categoria, color=colores[i])

ax.set_xticks(medias)
ax.set_xticklabels(categorias, rotation=45, ha='right')
ax.set_title('Intervalos de confianza para Mujeres (2000, 2010, 2015, 2020)')
ax.set_xlabel('Grado promedio de escolaridad')
ax.set_ylabel('Densidad de probabilidad')
ax.legend()
ax.grid(True)

plt.tight_layout()
plt.show()


# %% [markdown]
# ---
# ---
#  
# <font size="4"> 
# <span style="color:blue">
#  
# 4) Usando como referencia los datos  del archivo _**Educacion_modificada.xlsx**_ 
# 
#  
# <span style="color:red">
#  
# a) Realice una representación gráfica que involucre los histogramas asociados a _Hombres_, _Mujeres_  y _PROMEDIO TOTAL_ (en una sola ventana de graficación.)
#  
# b) Realice una representación gráfica que involucre los intervalos de confianza asociados a _Hombres_ y _Mujeres_  y _PROMEDIO TOTAL_ (en una sola ventana de graficación.)

# %% [markdown]
# <h1>a)</h1>

# %%

import matplotlib.pyplot as plt

df_combined = pd.concat([df_2000, df_2010, df_2015, df_2020])

plt.figure(figsize=(15, 10))

plt.subplot(3, 1, 1)
plt.hist(df_combined['Hombres'], bins=30, color='aqua', edgecolor='black', alpha=0.7)
plt.title('Histogramas de Hombres')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(3, 1, 2)
plt.hist(df_combined['Mujeres'], bins=30, color='pink', edgecolor='black', alpha=0.7)
plt.title('Histogramas de Mujeres')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)

plt.subplot(3, 1, 3)
plt.hist(df_combined['PROMEDIO TOTAL'], bins=30, color='lime', edgecolor='black', alpha=0.7)
plt.title('Histogramas de PROMEDIO TOTAL')
plt.xlabel('Grado promedio de escolaridad')
plt.ylabel('Frecuencia')
plt.grid(axis='y', alpha=0.5)


plt.tight_layout(rect=[0, 0.03, 1, 0.95])
plt.show()


# %% [markdown]
# <h1>b)</h1>

# %%

import pandas as pd
import numpy as np
from scipy import stats

filas_inicio_fin = {
    2000: (15, 47),
    2010: (47, 79),
    2015: (79, 111),
    2020: (111, 143)
}

dataframes = {}
for year, (inicio, fin) in filas_inicio_fin.items():
    filas_a_omitir = inicio - 1
    dataframes[year] = pd.read_excel('Educacion_modificada.xlsx', skiprows=filas_a_omitir, nrows=fin - filas_a_omitir - 1, engine='openpyxl')

for year in [2010, 2015, 2020]:
    dataframes[year].columns = dataframes[2000].columns

df_2000 = dataframes[2000]
df_2010 = dataframes[2010]
df_2015 = dataframes[2015]
df_2020 = dataframes[2020]

df_combined = pd.concat([df_2000, df_2010, df_2015, df_2020])

def intervalo_confianza(data, alpha=0.05):
    media = np.mean(data)
    sem = stats.sem(data)
    intervalo = stats.t.interval(1-alpha, len(data)-1, loc=media, scale=sem)
    return media, intervalo

media_hombres, intervalo_hombres = intervalo_confianza(df_combined['Hombres'])
media_mujeres, intervalo_mujeres = intervalo_confianza(df_combined['Mujeres'])
media_total, intervalo_total = intervalo_confianza(df_combined['PROMEDIO TOTAL'])

print(f'\nIntervalo de confianza para Hombres: {intervalo_hombres}')
print(f'Intervalo de confianza para Mujeres: {intervalo_mujeres}')
print(f'Intervalo de confianza para PROMEDIO TOTAL: {intervalo_total}')

import matplotlib.pyplot as plt
from scipy.stats import norm

fig, ax = plt.subplots(figsize=(16, 8))

categorias = ['Hombres', 'Mujeres', 'PROMEDIO TOTAL']
medias = [media_hombres, media_mujeres, media_total]
errores = [(intervalo_hombres[1]-intervalo_hombres[0])/2,
           (intervalo_mujeres[1]-intervalo_mujeres[0])/2,
           (intervalo_total[1]-intervalo_total[0])/2]

colores = ['aqua', 'pink', 'lime']

for i, categoria in enumerate(categorias):
    media = medias[i]
    error = errores[i]
    desv = error * np.sqrt(len(df_combined)) 
    x = np.linspace(media - 3*desv, media + 3*desv, 100)
    y = norm.pdf(x, media, desv)
    ax.plot(x, y, label=categoria, color=colores[i])

for i, media in enumerate(medias):
    ax.axvline(media, color=colores[i], linestyle='--')

ax.set_xticks(medias)
ax.set_xticklabels(categorias, rotation=45, ha='right', fontsize=12)
ax.set_title('Intervalos de confianza para Hombres, Mujeres y PROMEDIO TOTAL')
ax.set_xlabel('Grado promedio de escolaridad')
ax.set_ylabel('Densidad de probabilidad')
ax.legend()
ax.grid(True)

plt.tight_layout()
plt.show()




# %% [markdown]
# ---
# ---
#  
# <font size="4"> 
# <span style="color:blue">
#  
# 5) Usando como referencia los de datos  del archivo _**Educacion_modificada.xlsx**_ 
# 
#  
# <span style="color:red">
#  
#  
# a) Realice una representación gráfica que involucre los intervalo de confianza usando el siguiente orden: _Hombres-2000_, _Mujeres-2000_, _Hombres-2010_, _Mujeres-2010_,  _Hombres-2015_, _Mujeres-2015_, _Hombres-2020_, _Mujeres-2020_ (donde se visualizen los intervalos en forma vertical y en una sola ventana de graficación.)   

# %%

import numpy as np
from scipy import stats
import matplotlib.pyplot as plt
from scipy.stats import norm

def intervalo_confianza(data, alpha=0.05):
    media = np.mean(data)
    sem = stats.sem(data)
    intervalo = stats.t.interval(1-alpha, len(data)-1, loc=media, scale=sem)
    return media, intervalo

media_hombres_2000, intervalo_hombres_2000 = intervalo_confianza(df_2000['Hombres'])
media_mujeres_2000, intervalo_mujeres_2000 = intervalo_confianza(df_2000['Mujeres'])

media_hombres_2010, intervalo_hombres_2010 = intervalo_confianza(df_2010['Hombres'])
media_mujeres_2010, intervalo_mujeres_2010 = intervalo_confianza(df_2010['Mujeres'])

media_hombres_2015, intervalo_hombres_2015 = intervalo_confianza(df_2015['Hombres'])
media_mujeres_2015, intervalo_mujeres_2015 = intervalo_confianza(df_2015['Mujeres'])

media_hombres_2020, intervalo_hombres_2020 = intervalo_confianza(df_2020['Hombres'])
media_mujeres_2020, intervalo_mujeres_2020 = intervalo_confianza(df_2020['Mujeres'])

fig = plt.figure(1, figsize=(15, 6))
ax = fig.add_subplot(111)

categorias = ['Hombres-2000', 'Mujeres-2000', 'Hombres-2010', 'Mujeres-2010', 'Hombres-2015', 'Mujeres-2015', 'Hombres-2020', 'Mujeres-2020']
medias = [media_hombres_2000, media_mujeres_2000, media_hombres_2010, media_mujeres_2010, media_hombres_2015, media_mujeres_2015, media_hombres_2020, media_mujeres_2020]
errores = [(intervalo_hombres_2000[1]-intervalo_hombres_2000[0])/2,
           (intervalo_mujeres_2000[1]-intervalo_mujeres_2000[0])/2,
           (intervalo_hombres_2010[1]-intervalo_hombres_2010[0])/2,
           (intervalo_mujeres_2010[1]-intervalo_mujeres_2010[0])/2,
           (intervalo_hombres_2015[1]-intervalo_hombres_2015[0])/2,
           (intervalo_mujeres_2015[1]-intervalo_mujeres_2015[0])/2,
           (intervalo_hombres_2020[1]-intervalo_hombres_2020[0])/2,
           (intervalo_mujeres_2020[1]-intervalo_mujeres_2020[0])/2]

colores = ['gray', 'gray', 'green', 'green', 'orange', 'orange', 'aqua', 'aqua']

for i in range(len(categorias)):
    ax.plot([i, i], [medias[i] - errores[i], medias[i] + errores[i]], color=colores[i], lw=2)
    ax.plot(i, medias[i], 'o', color='red')

ax.set_xticks(range(len(categorias)))
ax.set_xticklabels(categorias, rotation=45)
ax.set_title('Intervalos de confianza para Hombres y Mujeres (2000, 2010, 2015, 2020)')
ax.set_xlabel('Categoría')
ax.set_ylabel('Grado promedio de escolaridad')
ax.grid(True)

plt.tight_layout()
plt.show()
 

# %%



