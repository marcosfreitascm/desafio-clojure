(ns desafio.model.compra
  (:require [schema.core :as s]
            [desafio.model.cartao :as cartao])
  (:import (java.util Date)))

(def Compra
  {:compra/id              s/Uuid
   :compra/data            Date
   :compra/valor           s/Num
   :compra/estabelecimento s/Str
   :compra/categoria       s/Str
   :compra/cartao          cartao/Cartao})

(def Solicitacao-de-Compra
  {:compra    (s/maybe Compra)
   :resultado s/Keyword})

(s/defn cria-nova-compra
  [data
   valor :- s/Num
   estabelecimento :- s/Str
   categoria :- s/Str
   cartao :- (s/maybe cartao/Cartao)]
  {:compra/id              (java.util.UUID/randomUUID)
   :compra/data            data
   :compra/valor           valor
   :compra/estabelecimento estabelecimento
   :compra/categoria       categoria
   :compra/cartao          cartao})


