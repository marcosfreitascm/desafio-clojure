(ns desafio.core
  (:require [desafio.logic.logic :as d.logic]
            [desafio.db.db :as d.db]
            [schema.core :as s])
  (:use [java-time :only [local-date local-date?]]
        clojure.pprint))

(s/set-fn-validation! true)

(d.db/apaga-banco!)
(def conn (d.db/abre-conexao!))
(d.db/cria-schema! conn)

(d.db/cria-dados-exemplo conn)

; Codigo já existente
(println "Clientes:")
(pprint (d.db/todos-os-clientes (d.db/snapshot-db conn)))

(println "Cartões:")
(pprint(d.db/todos-os-cartoes (d.db/snapshot-db conn)))

(let [cartao (d.db/obtem-cartao-por-numero (d.db/snapshot-db conn) 2555)
      compra (get (d.logic/adiciona-compra (java.util.Date.)
                                           1000M
                                           "Padaria Nova"
                                           "Alimentação"
                                           cartao) :compra)]
  (println "Adicionando compra:")
  (println compra)
  (d.db/adiciona-compras! conn [compra]))

(let [compras (d.db/todas-as-compras (d.db/snapshot-db conn))]
(println "Compras realizadas:")
(pprint compras)

(println "Gastos por categoria:")
(pprint (d.logic/total-compras-por-categoria compras))

(pprint "Compras por estabelecimento:")
(pprint (d.logic/compras-por-estabelecimento "Renner" compras))

(println "Gasto por mês:")
(println (d.logic/fatura-por-mes compras 7)))

