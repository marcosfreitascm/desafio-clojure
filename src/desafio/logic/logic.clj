(ns desafio.logic.logic
  (:require [desafio.db.db :as d.db]
            [schema.core :as s]
            [desafio.model.compra :as d.compra])
  (:use [java-time :only [as]]))

(defn retorna-todas-compras [] (d.db/retorna-todas-compras))

(defn retorna-clientes [] (d.db/retorna-clientes))

(defn retorna-cartoes [] (d.db/retorna-cartoes))

(defn total-das-compras
  [compras]
  (->> compras
       (map :valor compras)
       (reduce +)))

(defn gastos-compras-por-categoria
  [[categoria compras]]
  {:categoria   categoria
   :gasto-total (total-das-compras compras)
   })

(defn retorna-dados-cartao
  [cartao-id]
  (filter #(= (:cartao-id %) cartao-id) (retorna-cartoes)))

(defn retorna-dados-cartao-por-numero
  [numero]
  (first (filter #(= (:numero %) numero) (retorna-cartoes))))

(defn gastos-compras-por-cartao
  [[cartao compras]]
  {:cartao      (:numero (first (retorna-dados-cartao cartao)))
   :gasto-total (total-das-compras compras)
   }
  )

(defn comprou-no-mes?
  [compra mes]
  (= (as (:data compra) :month-of-year) mes))

(defn fatura-por-mes [mes] (->>
                             (d.db/retorna-todas-compras)
                             (filter #(comprou-no-mes? % mes))
                             (group-by :cartao-id)
                             (map gastos-compras-por-cartao)))




(defn total-compras-por-categoria [] (map
                                       gastos-compras-por-categoria
                                       (group-by :categoria (d.db/retorna-todas-compras))))


(defn compras-por-estabelecimento
  [estabelecimento] (filter (fn
                              [compra]
                              (identical? (:estabelecimento compra) estabelecimento))
                            (d.db/retorna-todas-compras)))

(s/defn adiciona-compra :- [d.compra/Compra]
  [compras :- [d.compra/Compra]
   data :- d.compra/LocalDate
   valor :- s/Num
   estabelecimento :- s/Str
   categoria :- s/Str
   numero :- s/Num]
  (let [cartao (retorna-dados-cartao-por-numero numero)]
    (if (>= (get cartao :limite) valor)
      {:compras   (conj compras
                        (d.compra/cria-nova-compra data valor estabelecimento categoria (get cartao :cartao-id)))
       :resultado :sucesso}
      {:compras   compras
       :resultado :limite-indisponivel})))


