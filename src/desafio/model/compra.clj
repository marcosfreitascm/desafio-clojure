(ns desafio.model.compra
  (:require [schema.core :as s])
  (:use [java-time :only [local-date local-date?]]))

(def LocalDate (s/pred local-date? 'tipo-de-data))
(def Compra
  {:compra-id s/Uuid
   :data LocalDate
   :valor s/Num
   :estabelecimento s/Str
   :categoria s/Str
   :cartao-id s/Uuid})

(s/defn cria-nova-compra :- Compra
  [data :- LocalDate
   valor :- s/Num
   estabelecimento :- s/Str
   categoria :- s/Str
   cartao-id :- s/Uuid]
  {:compra-id (str (java.util.UUID/randomUUID))
      :data      data
           :valor valor
           :estabelecimento estabelecimento
           :categoria categoria
            :cartao-id cartao-id})


