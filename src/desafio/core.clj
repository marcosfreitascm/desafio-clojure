(ns desafio.core
  (:require [desafio.logic.logic :as d.logic])
  (:use [java-time :only [local-date local-date?]]))


(println "Clientes:")
(println (d.logic/retorna-clientes))

(println "Cartões:")
(println (d.logic/retorna-cartoes))

(let [compras-realizadas (d.logic/retorna-todas-compras)]
  (println "Compras realizadas:")
  (println compras-realizadas)

  (println "Adicionando compras:")
  (println (get (d.logic/adiciona-compra compras-realizadas   (local-date 2021 8 3)
                                    1000
                                    "Padaria Nova"
                                    "Alimentação"
                                    2555) :compras)))

(println "Gastos por categoria:")
(println (d.logic/total-compras-por-categoria))

(println "Compras por estabelecimento:")
(println (d.logic/compras-por-estabelecimento "Renner"))

(println "Gasto por mês:")
(println (d.logic/fatura-por-mes "02"))

