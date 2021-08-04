(ns desafio.model.cartao
  (:require [schema.core :as s]))

(def Cartao
  {:cartao-id  s/Uuid
   :numero     s/Num
   :cvv        s/Num
   :validade   s/Str
   :limite     s/Num
   :cliente-id s/Uuid})

(s/defn cria-novo-cartao :- Cartao
  [numero :- s/Num
   cvv :- s/Num
   validade :- s/Str
   limite :- s/Num
   cliente-id :- s/Uuid]
  {:cartao-id  (str (java.util.UUID/randomUUID))
   :numero     numero
   :cvv        cvv
   :validade   validade
   :limite     limite
   :cliente-id cliente-id})





