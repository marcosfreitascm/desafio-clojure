(ns desafio.model.cartao
  (:require [schema.core :as s]
            [desafio.model.cliente :as cliente]))

(def Cartao
  {:cartao/id       s/Uuid
   :cartao/numero   s/Num
   :cartao/cvv      s/Num
   :cartao/validade s/Str
   :cartao/limite   s/Num
   :cartao/cliente  cliente/Cliente})

(s/defn cria-novo-cartao
  [numero :- s/Num
   cvv :- s/Num
   validade :- s/Str
   limite :- s/Num
   cliente :- (s/maybe cliente/Cliente)]
  {:cartao/id       (java.util.UUID/randomUUID)
   :cartao/numero   numero
   :cartao/cvv      cvv
   :cartao/validade validade
   :cartao/limite   limite
   :cartao/cliente  cliente})





