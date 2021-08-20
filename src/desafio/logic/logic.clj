(ns desafio.logic.logic
  (:require [schema.core :as s]
            [desafio.model.compra :as d.compra]
            [desafio.model.cartao :as d.cartao])
  (:import (java.util Date))
  (:use [java-time :only [local-date as instant zone-id]]))

(defn total-das-compras
  [compras]
  (->> compras
       (map :compra/valor compras)
       (reduce +)))

(defn gastos-compras-por-categoria
  [[categoria compras]]
  {:categoria   categoria
   :gasto-total (total-das-compras compras)
   })

(defn gastos-compras-por-cartao
  [[cartao compras]]
  {:cartao      (:cartao/numero cartao)
   :gasto-total (total-das-compras compras)})

(defn comprou-no-mes?
  [compra mes]
  (= (as (local-date (:compra/data compra) (zone-id)) :month-of-year) mes))

(defn fatura-por-mes [compras mes] (->>
                                     compras
                                     (filter #(comprou-no-mes? % mes))
                                     (group-by :compra/cartao)
                                     (map gastos-compras-por-cartao)))

(defn total-compras-por-categoria [compras] (map
                                              gastos-compras-por-categoria
                                              (group-by :compra/categoria compras)))

(defn compras-por-estabelecimento
  [estabelecimento compras] (filter (fn
                                      [compra]
                                      (= (:compra/estabelecimento compra) estabelecimento))
                                    compras))

(s/defn adiciona-compra :- d.compra/Solicitacao-de-Compra
  [data :- Date
   valor :- s/Num
   estabelecimento :- s/Str
   categoria :- s/Str
   cartao :- (s/maybe d.cartao/Cartao)]
  (if (>= (get cartao :cartao/limite) valor)
    (do
      (let [compra (d.compra/cria-nova-compra data valor estabelecimento categoria cartao)]
        (println compra)
        {:compra    compra
         :resultado :sucesso}))
    {:compra    nil
     :resultado :limite-indisponivel}))