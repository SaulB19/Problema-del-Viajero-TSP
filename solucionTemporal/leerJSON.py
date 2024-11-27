from json import load
from math import sqrt
from csv import DictWriter
from sys import stdout


def main():
    objeto = cargarJSON()

    estados: list[dict[str]] = interpretar(objeto)

    writer = DictWriter(
        stdout, [""] + [estado["Nombre"] for estado in estados], lineterminator="\n"
    )

    writer.writeheader()

    for estado in estados:
        linea = estado["Distancias"]
        for enlace in linea:
            linea[enlace] += f";{enlace in estado['Colindantes']}"

        linea[""] = estado["Nombre"]

        writer.writerow(linea)


def cargarJSON():
    with open("datos/us-states.json", "r") as inf:
        obj = load(inf)

    return obj


def interpretar(objeto) -> list[dict[str]]:
    estados: list[dict[str]] = []
    for feature in objeto["features"]:
        estado: dict[str] = {}
        estado["Nombre"] = feature["properties"]["name"]
        estado["Centro"] = obtenerCentro(
            feature["geometry"]["type"], feature["geometry"]["coordinates"]
        )

        estado["Puntos"] = reunirPuntos(
            feature["geometry"]["type"], feature["geometry"]["coordinates"]
        )

        estados.append(estado)

    for origen in estados:
        distancias: dict[str, float] = {}

        for destino in estados:
            if origen is destino:
                distancias[destino["Nombre"]] = "0"
                continue

            x1, y1 = origen["Centro"]
            x2, y2 = destino["Centro"]

            # TODO: Usar la formula de Haversine para calcular la distancia en lugar del teorema de Pitagoras
            distancia: float = sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2)

            distancias[destino["Nombre"]] = str(distancia)

        origen["Distancias"] = distancias

        origen["Colindantes"] = obtenerColindantes(origen, estados)

    return estados


def obtenerCentro(tipo: str, figuras: list) -> tuple[float, float]:
    match tipo:
        case "Polygon":
            listaX = []
            listaY = []
            for punto in figuras[0]:
                listaX.append(punto[0])
                listaY.append(punto[1])

            centroX = (max(listaX) + min(listaX)) / 2
            centroY = (max(listaY) + min(listaY)) / 2

            return (centroX, centroY)

        case "MultiPolygon":
            tamanios = {}
            for figura in figuras:
                figura = figura[0]

                listaX = []
                listaY = []
                for punto in figura:
                    listaX.append(punto[0])
                    listaY.append(punto[1])

                centroX = (max(listaX) + min(listaX)) / 2
                centroY = (max(listaY) + min(listaY)) / 2

                centro = (centroX, centroY)

                tamanios[(max(listaX) - min(listaX)) * (max(listaY) - min(listaY))] = (
                    centro
                )

            return tamanios[max(tamanios.keys())]

        case _:
            raise ValueError("Tipo aun no soportado")


def reunirPuntos(tipo: str, figuras: list) -> tuple[float, float]:
    puntos: set[float] = set()

    match tipo:
        case "Polygon":
            for punto in figuras[0]:
                puntos.add(tuple(punto))

        case "MultiPolygon":
            for figura in figuras:
                for punto in figura[0]:
                    puntos.add(tuple(punto))

        case _:
            raise ValueError("Tipo aun no soportado")

    return puntos


def obtenerColindantes(estado: dict[str], estados_totales: list[dict[str]]) -> list[str]:
    colindantes = []
    for otro in estados_totales:
        if estado is otro:
            continue

        puntos_en_comun: set = estado["Puntos"].intersection(otro["Puntos"])
        if len(puntos_en_comun) > 0:
            colindantes.append(otro["Nombre"])

    return colindantes


if __name__ == "__main__":
    main()
